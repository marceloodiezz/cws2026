package uo.ri.cws.application.service.client.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.command.CommandExecutor;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.crud.commands.FindClientByNif;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.NotYetImplementedException;

public class ClientCrudServiceImpl implements ClientCrudService {
    
    private CommandExecutor executor = new CommandExecutor();
    
    @Override
    public ClientDto addClient(ClientDto client, String recommenderNif) throws BusinessException {
        throw new NotYetImplementedException();
    }
    
    @Override
    public void deleteClientByNif(String nif) throws BusinessException {
        throw new NotYetImplementedException();
    }
    
    @Override
    public void updateClient(ClientDto client) throws BusinessException {
        throw new NotYetImplementedException();
    }
    
    @Override
    public List<ClientDto> findAllClients() throws BusinessException {
        throw new NotYetImplementedException();
    }
    
    @Override
    public Optional<ClientDto> findClientByNif(String nif) throws BusinessException {
        return executor.execute(new FindClientByNif(nif));
    }
    
    @Override
    public List<ClientDto> findClientsRecommendedBy(String recommenderNif) throws BusinessException {
        throw new NotYetImplementedException();
    }
    
}
