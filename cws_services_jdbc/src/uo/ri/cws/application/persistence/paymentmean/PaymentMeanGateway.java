package uo.ri.cws.application.persistence.paymentmean;

import java.math.BigDecimal;
import java.util.List;

import uo.ri.cws.application.persistence.Gateway;

public interface PaymentMeanGateway extends Gateway<PaymentMeanGateway.PaymentMeanRecord> {
    
    List<PaymentMeanRecord> findByClientId(String clientId);
    
    public class PaymentMeanRecord extends Gateway.BaseRecord {
        
        public String dtype;
        public String clientId;
        public BigDecimal accumulated;
        
    }

}
