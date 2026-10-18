package uo.ri.cws.application.persistence.creditcard;

import java.time.LocalDate;
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;

public interface CreditCardGateway extends Gateway<CreditCardGateway.CreditCardRecord> {
    
    Optional<CreditCardRecord> findByNumber(String number);
    
    public class CreditCardRecord extends Gateway.BaseRecord {
        
        public String number;
        public String type;
        public LocalDate validThru;
        
    }
    
}
