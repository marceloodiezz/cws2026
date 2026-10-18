package uo.ri.cws.application.service.paymentmean.crud;

import java.time.YearMonth;

import uo.ri.cws.application.persistence.creditcard.CreditCardGateway.CreditCardRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.voucher.VoucherGateway.VoucherRecord;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CashDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;

public class PaymentMeanDtoAssembler {
    
    public static CashDto toCashDto(PaymentMeanRecord pm) {
        CashDto dto = new CashDto();
        
        dto.id = pm.id;
        dto.version = pm.version;
        dto.clientId = pm.clientId;
        dto.accumulatedAmount = pm.accumulated;
        
        return dto;
    }
    
    public static CardDto toCardDto(PaymentMeanRecord pm, CreditCardRecord card) {
        CardDto dto = new CardDto();
        
        dto.id = pm.id;
        dto.version = pm.version;
        dto.clientId = pm.clientId;
        dto.accumulatedAmount = pm.accumulated;
        
        dto.cardNumber = card.number;
        dto.cardType = card.type;
        dto.cardExpiration = YearMonth.from(card.validThru);
        
        return dto;
    }
    
    public static VoucherDto toVoucherDto(PaymentMeanRecord pm, VoucherRecord voucher) {
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
