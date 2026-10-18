package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.cws.application.persistence.voucher.VoucherGateway.VoucherRecord;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.cws.application.service.paymentmean.crud.PaymentMeanDtoAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindVoucherByCode implements Command<Optional<VoucherDto>> {
    
    private VoucherGateway vg = Factories.persistence.forVoucher();
    
    private PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();
    
    private String code;
    
    public FindVoucherByCode(String code) {
        ArgumentChecks.isNotNull(code, "The code cannot be null");
        ArgumentChecks.isNotBlank(code, "The code cannot be blank");
        
        this.code = code;
    }
    
    @Override
    public Optional<VoucherDto> execute() throws BusinessException {
        Optional<VoucherRecord> voucher = vg.findByCode(code);
        
        if (voucher.isEmpty()) {
            return Optional.empty();
        }
        
        VoucherRecord vr = voucher.get();
        PaymentMeanRecord pm = pmg.findById(vr.id)
                .orElseThrow(() -> new IllegalStateException("Payment mean not found for voucher: " + vr.id));
        
        VoucherDto dto = PaymentMeanDtoAssembler.toVoucherDto(pm, vr);
        
        return Optional.of(dto);
    }
    
}
