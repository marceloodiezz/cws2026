package uo.ri.cws.application.service.client.crud;

import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;

public class ClientDtoAssembler {
    
    public static ClientDto toDto(ClientRecord record) {
        ClientDto dto = new ClientDto();
        
        dto.id = record.id;
        dto.version = record.version;
        
        dto.nif = record.nif;
        dto.name = record.name;
        dto.surname = record.surname;
        
        dto.addressStreet = record.street;
        dto.addressCity = record.city;
        dto.addressCountry = record.country;
        dto.addressZipcode = record.postalCode;
        
        dto.phone = record.phone;
        dto.email = record.email;
        
        return dto;
    }
    
}
