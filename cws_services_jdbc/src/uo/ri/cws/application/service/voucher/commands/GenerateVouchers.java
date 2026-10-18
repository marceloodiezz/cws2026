package uo.ri.cws.application.service.voucher.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway.RecommendationRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.cws.application.persistence.voucher.VoucherGateway.VoucherRecord;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.util.exception.BusinessException;

public class GenerateVouchers implements Command<int[]> {

    private static final int RECOMMENDATIONS_PER_VOUCHER = 3;
    private static final BigDecimal VOUCHER_AMOUNT = new BigDecimal("25.00");
    private static final String VOUCHER_DESCRIPTION = "Por recomendación";

    private ClientGateway cg = Factories.persistence.forClient();

    private RecommendationGateway rg = Factories.persistence.forRecommendation();

    private WorkOrderGateway wg = Factories.persistence.forWorkOrder();

    private PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();

    private VoucherGateway vg = Factories.persistence.forVoucher();

    @Override
    public int[] execute() throws BusinessException {
        int[] counters = { 0, 0, 0 };

        List<ClientRecord> clients = cg.findAll();

        for (ClientRecord client : clients) {

            if (!wg.hasPaidWorkOrdersByClientId(client.id))
                continue;

            List<RecommendationRecord> recommendations = rg.findAvailableByRecommender(client.id);
            List<RecommendationRecord> validRecommendations = new ArrayList<>();

            for (RecommendationRecord recommendation : recommendations)
                if (wg.hasPaidWorkOrdersByClientId(recommendation.recommendedId))
                    validRecommendations.add(recommendation);

            int numberOfVouchers = validRecommendations.size() / RECOMMENDATIONS_PER_VOUCHER;

            for (int i = 0; i < numberOfVouchers; i++) {
                generateVoucher(client.id);

                int start = i * RECOMMENDATIONS_PER_VOUCHER;

                for (int j = start; j < start + RECOMMENDATIONS_PER_VOUCHER; j++) {
                    RecommendationRecord recommendation = validRecommendations.get(j);

                    recommendation.usedForVoucher = true;
                    recommendation.updatedAt = LocalDateTime.now();

                    rg.update(recommendation);
                }

                counters[1]++;
            }
        }

        return counters;
    }

    private void generateVoucher(String clientId) {
        PaymentMeanRecord pm = new PaymentMeanRecord();

        pm.dtype = "Voucher";
        pm.clientId = clientId;
        pm.accumulated = BigDecimal.ZERO;

        VoucherRecord voucher = new VoucherRecord();

        voucher.id = pm.id;
        voucher.code = UUID.randomUUID().toString();
        voucher.description = VOUCHER_DESCRIPTION;
        voucher.available = VOUCHER_AMOUNT;

        pmg.add(pm);
        vg.add(voucher);
    }
}
