package uo.ri.cws.application.ui.manager.mechanic.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class UpdateMechanicAction implements Action {
    
    private MechanicCrudService service = Factories.service.forMechanicCrudService();
    
    @Override
    public void execute() throws BusinessException {
        String id = InputReader.readRequiredString("Type mechanic id to update");
        
        Optional<MechanicDto> mechanic = service.findById(id);
        
        BusinessChecks.exists(mechanic, "Mechanic does not exist");
        
        MechanicDto dto = mechanic.get();
        
        dto.name = InputReader.readRequiredString("Name");
        dto.surname = InputReader.readRequiredString("Surname");
        dto.nif = InputReader.readRequiredString("NIF");
        
        service.update(dto);
        
        Console.println("Mechanic updated");
    }
    
}
