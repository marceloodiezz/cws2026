package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.MechanicDtoAssembler;
import uo.ri.util.assertion.ArgumentChecks;

public class FindById implements Command<Optional<MechanicDto>> {
    
    private MechanicGateway mg = Factories.persistence.forMechanic();
	
	private String id;
	
	public FindById(String id) {
		ArgumentChecks.isNotNull(id, "The id cannot be null");
		this.id = id;
	}
	
	@Override
    public Optional<MechanicDto> execute() {
	    
	    Optional<MechanicRecord> record = mg.findById(id);
    
        if (record.isEmpty())
            return Optional.empty();
        
        MechanicDto dto = MechanicDtoAssembler.toDto(record.get());
    
        return Optional.of(dto);
	}

}
