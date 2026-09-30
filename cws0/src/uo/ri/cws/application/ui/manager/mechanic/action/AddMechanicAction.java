package uo.ri.cws.application.ui.manager.mechanic.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.UserInteractionChecks;
import uo.ri.util.exception.UserInteractionException;
import uo.ri.util.menu.Action;

public class AddMechanicAction implements Action {
	
	private MechanicCrudService service = Factories.service.forMechanicCrudService();
    

    @Override
    public void execute() throws BusinessException, UserInteractionException {

    	MechanicDto dto = new MechanicDto();
    	
        dto.nif = Console.readString("nif");
        dto.name = Console.readString("Name");
        dto.surname = Console.readString("Surname");
       
        UserInteractionChecks.isFalse(dto.nif.isBlank(), "Invalid NIF");
        UserInteractionChecks.isFalse(dto.name.isBlank(), "Invalid name");
        UserInteractionChecks.isFalse(dto.surname.isBlank(), "Invalid surname");
        
        dto = service.create(dto);

        // Print result
        Console.println("Mechanic added");
    }

}
