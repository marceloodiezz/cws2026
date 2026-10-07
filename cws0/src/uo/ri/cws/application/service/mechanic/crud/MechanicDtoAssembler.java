package uo.ri.cws.application.service.mechanic.crud;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;

public class MechanicDtoAssembler {
    // Para transmitir datos de una capa a otra necesitamos una serie de DTOs o RECORDs.
    // Esta clase está dentro de la capa de servicio
    // Sirve apra crear el RECORD que necesita la Gateway
    
    public static MechanicRecord toRecord(MechanicDto dto) {
        MechanicRecord record = new MechanicRecord();
        
        record.nif = dto.nif;
        record.name = dto.name;
        record.surname = dto.surname;
        
        return record;
    }
    
    public static MechanicRecord toRecordForUpdate(MechanicDto dto) {
        MechanicRecord record = new MechanicRecord();
    
        record.id = dto.id;
        record.version = dto.version;
        record.nif = dto.nif;
        record.name = dto.name;
        record.surname = dto.surname;
        record.updatedAt = LocalDateTime.now();
    
        return record;
    }
    
    public static MechanicDto toDto(MechanicRecord record) {
        MechanicDto dto = new MechanicDto();
        
        dto.id = record.id;
        dto.nif = record.nif;
        dto.name = record.name;
        dto.surname = record.surname;
        dto.version = record.version;
        
        return dto;
    }
    
    public static List<MechanicDto> toDtoList(List<MechanicRecord> records) {
        List<MechanicDto> result = new ArrayList<>();
    
        for (MechanicRecord record : records)
            result.add(toDto(record));
    
        return result;
    }

}
