package uo.ri.cws.application.persistence.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.util.exception.PersistenceException;

public interface InvoiceGateway extends Gateway<InvoiceGateway.InvoiceRecord> {
    
    long getNextInvoiceNumber() throws PersistenceException;
    
    public class InvoiceRecord extends Gateway.BaseRecord {

        public long number;
        public LocalDate date;
        public String state;

        public BigDecimal subtotal;
        public BigDecimal total;

        public double vatRate;
        public BigDecimal vatAmount;
        
    }

}
