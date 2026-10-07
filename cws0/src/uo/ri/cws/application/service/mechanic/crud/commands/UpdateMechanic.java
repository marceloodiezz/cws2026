package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.MechanicDtoAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateMechanic implements Command<Void> {
    
    private MechanicGateway mg = Factories.persistence.forMechanic();
    
    private MechanicDto dto;
    
    public UpdateMechanic(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto, "The mechanic cannot be null");
        ArgumentChecks.isNotBlank(dto.id, "The id cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.nif, "The nif cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.name, "The name cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.surname, "The surname cannot be null or blank");
        
        this.dto = dto;
    }

    @Override
    public Void execute() throws BusinessException {
        
        Optional<MechanicRecord> current = mg.findById(dto.id);
        
        BusinessChecks.exists(
            current,
            "Mechanic does not exist"
        );
        
        BusinessChecks.hasVersion(
            dto.version,
            current.get().version
        );
        
        MechanicRecord record = MechanicDtoAssembler.toRecordForUpdate(dto);
    
        mg.update(record);
    
        return null;
    }
    
}
