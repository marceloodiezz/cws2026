package uo.ri.cws.application.service.voucher.commands;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.cws.application.persistence.voucher.VoucherGateway.VoucherRecord;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;
import uo.ri.util.exception.BusinessException;

public class GetVoucherSummary implements Command<List<VoucherSummaryDto>> {
    
    private ClientGateway cg = Factories.persistence.forClient();
    
    private PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();
    
    private VoucherGateway vg = Factories.persistence.forVoucher();
    
    @Override
    public List<VoucherSummaryDto> execute() throws BusinessException {
        List<VoucherSummaryDto> result = new ArrayList<>();
        
        List<PaymentMeanRecord> paymentMeans = pmg.findAll();
        
        Map<String, List<PaymentMeanRecord>> vouchersByClient = new HashMap<>();
        
        for (PaymentMeanRecord pm : paymentMeans) {
            if (!"Voucher".equals(pm.dtype)) {
                continue;
            }
            
            List<PaymentMeanRecord> clientVouchers = vouchersByClient.get(pm.clientId);
            
            if (clientVouchers == null) {
                clientVouchers = new ArrayList<>();
                vouchersByClient.put(pm.clientId, clientVouchers);
            }
            
            clientVouchers.add(pm);
        }
        
        for (String clientId : vouchersByClient.keySet()) {
            
            ClientRecord client = cg.findById(clientId)
                    .orElseThrow(() -> new IllegalStateException("Client not found: " + clientId));
            
            List<PaymentMeanRecord> clientVouchers = vouchersByClient.get(clientId);
            
            long issuedQty = clientVouchers.size();
            
            BigDecimal availableAmount = BigDecimal.ZERO;
            BigDecimal consumedAmount = BigDecimal.ZERO;
            
            for (PaymentMeanRecord pm : clientVouchers) {
                
                VoucherRecord voucher = vg.findById(pm.id)
                        .orElseThrow(() -> new IllegalStateException("Voucher not found: " + pm.id));
                
                availableAmount =availableAmount.add(voucher.available);
                consumedAmount = consumedAmount.add(pm.accumulated);
            }
            
            BigDecimal totalAmount = availableAmount.add(consumedAmount);
            
            VoucherSummaryDto dto = new VoucherSummaryDto(
                    client.nif,
                    client.name,
                    client.surname,
                    issuedQty,
                    totalAmount,
                    availableAmount,
                    consumedAmount
            );
            
            result.add(dto);
        }
        
        result.sort(Comparator.comparing(VoucherSummaryDto::availableAmount).reversed());
        
        return result;
    }
    
}
