package uo.ri.cws.application.persistence.workorder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import uo.ri.util.exception.PersistenceException;

public interface WorkOrderGateway {
    
    // Para DeleteMechanic -------------------------
    
    boolean hasWorkOrders(String mechanicId) throws PersistenceException;
    
    
    // Para InvoiceWorkorder -----------------------
    
    boolean exists(String id) throws PersistenceException;
    
    String findState(String id) throws PersistenceException;
    
    BigDecimal findAmount(String id) throws PersistenceException;
    
    void linkToInvoice(String workOrderId, String invoiceId) throws PersistenceException;
    
    void markAsInvoiced(String workOrderId) throws PersistenceException;
    
    void incrementVersion(String workOrderId) throws PersistenceException;
    
    void updateTimestamp(String workOrderId, LocalDateTime timestamp) throws PersistenceException;
    
    
    // Para FindNotInvoicedWorkOrdersByClient ------
    
    List<InvoicingWorkOrderRecord> findNotInvoicedByClientNif(String nif) throws PersistenceException;
    
    public class InvoicingWorkOrderRecord {

        public String id;
        public String description;
        public LocalDateTime date;
        public String state;
        public BigDecimal amount;
        
    }

}
