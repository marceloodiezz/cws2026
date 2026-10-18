package uo.ri.cws.application.service.paymentmean.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.charge.ChargeGateway;
import uo.ri.cws.application.persistence.creditcard.CreditCardGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeletePaymentMean implements Command<Void> {
    
    private PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();
    
    private CreditCardGateway ccg = Factories.persistence.forCreditCard();
    
    private VoucherGateway vg = Factories.persistence.forVoucher();
    
    private ChargeGateway chg = Factories.persistence.forCharge();
    
    private String id;
    
    public DeletePaymentMean(String id) {
        ArgumentChecks.isNotNull(id, "The id cannot be null");
        ArgumentChecks.isNotBlank(id, "The id cannot be blank");
        
        this.id = id;
    }
    
    @Override
    public Void execute() throws BusinessException {
        
        PaymentMeanRecord pm = pmg.findById(id)
                .orElseThrow(() -> new BusinessException("Payment mean does not exist"));
        
        BusinessChecks.isFalse("Cash".equals(pm.dtype), "Cash payment means cannot be removed");
        
        BusinessChecks.isTrue(chg.countByPaymentMeanId(id) == 0, "Payment mean has charges");
        
        switch (pm.dtype) {
        
        case "CreditCard":
            ccg.remove(id);
            break;

        case "Voucher":
            vg.remove(id);
            break;
            
        default:
            throw new IllegalStateException("Unknown payment mean type: " + pm.dtype);
            
        }
        
        pmg.remove(id);
        
        return null;
    }
    
}
