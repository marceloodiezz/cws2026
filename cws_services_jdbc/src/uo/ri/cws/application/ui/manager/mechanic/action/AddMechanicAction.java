package uo.ri.cws.application.ui.manager.mechanic.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddMechanicAction implements Action {
	
	private MechanicCrudService service = Factories.service.forMechanicCrudService();
    
	@Override
	public void execute() throws BusinessException {
	    MechanicDto dto = new MechanicDto();
	    
	    dto.nif = InputReader.readRequiredString("NIF");
        dto.name = InputReader.readRequiredString("Name");
        dto.surname = InputReader.readRequiredString("Surname");
	    
	    service.create(dto);
	    
	    Console.println("Mechanic added");
	}
	
}
