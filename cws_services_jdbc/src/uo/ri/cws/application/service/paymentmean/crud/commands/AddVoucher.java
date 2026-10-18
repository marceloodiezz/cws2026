package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.math.BigDecimal;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.cws.application.persistence.voucher.VoucherGateway.VoucherRecord;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddVoucher implements Command<VoucherDto> {

    private ClientGateway cg = Factories.persistence.forClient();

    private PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();

    private VoucherGateway vg = Factories.persistence.forVoucher();

    private VoucherDto dto;

    public AddVoucher(VoucherDto dto) {
        ArgumentChecks.isNotNull(dto, "The voucher cannot be null");
        ArgumentChecks.isNotBlank(dto.clientId, "The client id cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.code, "The code cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.description, "The description cannot be null or blank");
        ArgumentChecks.isNotNull(dto.availableAmount, "The available amount cannot be null");
        ArgumentChecks.isTrue(dto.availableAmount.compareTo(BigDecimal.ZERO) >= 0,
                    "The available amount cannot be negative");

        this.dto = dto;
    }

    @Override
    public VoucherDto execute() throws BusinessException {
        BusinessChecks.exists(cg.findById(dto.clientId), "Client does not exist");

        BusinessChecks.doesNotExist(vg.findByCode(dto.code), "Voucher code already exists");

        PaymentMeanRecord pm = new PaymentMeanRecord();
        pm.dtype = "Voucher";
        pm.clientId = dto.clientId;
        pm.accumulated = BigDecimal.ZERO;

        VoucherRecord voucher = new VoucherRecord();
        voucher.id = pm.id;
        voucher.code = dto.code;
        voucher.description = dto.description;
        voucher.available = dto.availableAmount;

        pmg.add(pm);
        vg.add(voucher);

        dto.id = pm.id;
        dto.version = pm.version;
        dto.accumulatedAmount = pm.accumulated;

        return dto;
    }
}
