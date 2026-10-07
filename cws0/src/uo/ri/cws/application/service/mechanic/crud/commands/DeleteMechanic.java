package uo.ri.cws.application.service.mechanic.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.intervention.InterventionGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteMechanic implements Command<Void> {
    
    private MechanicGateway mg = Factories.persistence.forMechanic();
    
    private WorkOrderGateway wg = Factories.persistence.forWorkOrder();
    
    private InterventionGateway ig = Factories.persistence.forIntervention();
    
    private String mechanicId;
    
    public DeleteMechanic(String mechanicId) {
        ArgumentChecks.isNotNull(mechanicId);
        this.mechanicId = mechanicId;
    }
    
    @Override
    public Void execute() throws BusinessException {
        
        BusinessChecks.exists(
            mg.findById(mechanicId),
            "Mechanic does not exist"
        );
        
        BusinessChecks.isFalse(
            wg.hasWorkOrders(mechanicId),
            "Mechanic has work orders"
        );

        BusinessChecks.isFalse(
            ig.hasInterventions(mechanicId),
            "Mechanic has interventions"
        );
        
        mg.remove(mechanicId);

        return null;
        
    }

}
