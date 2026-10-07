package uo.ri.cws.application.persistence.intervention;

import uo.ri.util.exception.PersistenceException;

public interface InterventionGateway {
    
    boolean hasInterventions(String mechanicId) throws PersistenceException;

}
