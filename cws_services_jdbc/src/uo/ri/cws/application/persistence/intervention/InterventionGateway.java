package uo.ri.cws.application.persistence.intervention;

import java.time.LocalDateTime;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.util.exception.PersistenceException;

public interface InterventionGateway extends Gateway<InterventionGateway.InterventionRecord> {
    
    boolean hasInterventions(String mechanicId) throws PersistenceException;
    
    public class InterventionRecord extends Gateway.BaseRecord {
    
        public LocalDateTime date;
        public int minutes;
        public String mechanic_id;
        public String workOrder_id;
        
    }

}
