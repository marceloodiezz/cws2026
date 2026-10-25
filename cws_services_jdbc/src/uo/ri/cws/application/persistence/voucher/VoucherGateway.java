package uo.ri.cws.application.persistence.voucher;

import java.math.BigDecimal;
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;

public interface VoucherGateway extends Gateway<VoucherGateway.VoucherRecord> {
    
    Optional<VoucherRecord> findByCode(String code);
    
    public class VoucherRecord extends PaymentMeanRecord {
        
        public String code;
        public String description;
        public BigDecimal available;
        
    }
    
}
