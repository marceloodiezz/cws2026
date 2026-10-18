package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.creditcard.CreditCardGateway;
import uo.ri.cws.application.persistence.creditcard.CreditCardGateway.CreditCardRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.cws.application.persistence.voucher.VoucherGateway.VoucherRecord;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.cws.application.service.paymentmean.crud.PaymentMeanDtoAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindByClientNif implements Command<List<PaymentMeanDto>> {
    
    private ClientGateway cg = Factories.persistence.forClient();
    
    private PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();
    
    private CreditCardGateway ccg = Factories.persistence.forCreditCard();
    
    private VoucherGateway vg = Factories.persistence.forVoucher();
    
    private String nif;
    
    public FindByClientNif(String nif) {
        ArgumentChecks.isNotNull(nif, "The nif cannot be null");
        ArgumentChecks.isNotBlank(nif, "The nif cannot be blank");
        
        this.nif = nif;
    }
    
    @Override
    public List<PaymentMeanDto> execute() throws BusinessException {
        List<PaymentMeanDto> result = new ArrayList<>();
        
        Optional<ClientRecord> client = cg.findByNif(nif);
        
        if (client.isEmpty()) {
            return result;
        }
        
        List<PaymentMeanRecord> paymentMeans = pmg.findByClientId(client.get().id);
        
        for (PaymentMeanRecord pm : paymentMeans) {
            switch (pm.dtype) {
            
            case "Cash":
                result.add(PaymentMeanDtoAssembler.toCashDto(pm));
                break;
                
            case "CreditCard":
                CreditCardRecord card = ccg.findById(pm.id)
                            .orElseThrow(() -> new IllegalStateException("Credit card not found: " + pm.id));
                result.add(PaymentMeanDtoAssembler.toCardDto(pm, card));
                break;
                
            case "Voucher":
                VoucherRecord voucher = vg.findById(pm.id)
                            .orElseThrow(() -> new IllegalStateException("Voucher not found: " + pm.id));
                result.add(PaymentMeanDtoAssembler.toVoucherDto(pm, voucher));
                break;
                
            default:
                throw new IllegalStateException("Unknown payment mean type: " + pm.dtype);
                
            }
        }
        
        return result;
    }
    
}