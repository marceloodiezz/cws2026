package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.MechanicDtoAssembler;
import uo.ri.util.assertion.ArgumentChecks;

public class ListMechanic implements Command<Optional<MechanicDto>> {
    
    private MechanicGateway mg = Factories.persistence.forMechanic();
    
    private String nif;

    public ListMechanic(String nif) {
        ArgumentChecks.isNotNull(nif, "The nif cannot be null");
        this.nif = nif;
    }
    
    @Override
    public Optional<MechanicDto> execute() {
        
        Optional<MechanicRecord> record = mg.findByNif(nif);
        
        if (record.isEmpty())
            return Optional.empty();
        
        MechanicDto dto = MechanicDtoAssembler.toDto(record.get());
        
        return Optional.of(dto);
    }

}
