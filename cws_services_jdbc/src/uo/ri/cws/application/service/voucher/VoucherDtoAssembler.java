package uo.ri.cws.application.service.voucher;

import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.voucher.VoucherGateway.VoucherRecord;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;

public class VoucherDtoAssembler {
    
    public static VoucherDto toDto(PaymentMeanRecord pm, VoucherRecord voucher) {
        VoucherDto dto = new VoucherDto();
        
        dto.id = pm.id;
        dto.version = pm.version;
        dto.clientId = pm.clientId;
        dto.accumulatedAmount = pm.accumulated;
        
        dto.code = voucher.code;
        dto.description = voucher.description;
        dto.availableAmount = voucher.available;
        
        return dto;
    }
    
}
