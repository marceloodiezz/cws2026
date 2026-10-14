package uo.ri.cws.application.persistence.charge;

import java.math.BigDecimal;

import uo.ri.cws.application.persistence.Gateway;

public interface ChargeGateway extends Gateway<ChargeGateway.ChargeRecord> {
    
    int countByPaymentMeanId(String paymentMeanId);
    
    public class ChargeRecord extends Gateway.BaseRecord {
        
        public BigDecimal charge;
        public String invoiceId;
        public String paymentMeanId;
        
    }

}
