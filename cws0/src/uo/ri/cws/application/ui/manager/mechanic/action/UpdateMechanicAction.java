package uo.ri.cws.application.ui.manager.mechanic.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class UpdateMechanicAction implements Action {
    
    private MechanicCrudService service = Factories.service.forMechanicCrudService();

    @Override
    public void execute() throws BusinessException {

        // Get info
        String id = Console.readString("Type mechahic id to update");
        
        Optional<MechanicDto> mechanic = service.findById(id);
    
        BusinessChecks.exists(mechanic, "Mechanic does not exist");
        
        MechanicDto dto = mechanic.get();
        
        dto.name = Console.readString("Name");
        dto.surname = Console.readString("Surname");
        dto.nif = Console.readString("NIF");

        // update
        service.update(dto);
        
        // Print result
        Console.println("Mechanic updated");
    }
}