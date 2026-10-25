package uo.ri.cws.application.service.client.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.client.crud.ClientDtoAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindClientByNif implements Command<Optional<ClientDto>> {
    
    private ClientGateway cg = Factories.persistence.forClient();
    
    private String nif;
    
    public FindClientByNif(String nif) {
        ArgumentChecks.isNotNull(nif, "The nif cannot be null");
        ArgumentChecks.isNotBlank(nif, "The nif cannot be blank");
        
        this.nif = nif;
    }
    
    @Override
    public Optional<ClientDto> execute() throws BusinessException {
        Optional<ClientRecord> client = cg.findByNif(nif);
        
        if (client.isEmpty()) {
            return Optional.empty();
        }
        
        return Optional.of(ClientDtoAssembler.toDto(client.get()));
    }
    
}
