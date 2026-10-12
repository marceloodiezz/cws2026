package uo.ri.cws.application.service.invoice.create;

import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.InvoicingWorkOrderRecord;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;

public class InvoicingWorkOrderDtoAssembler {
    
    public static InvoicingWorkOrderDto toDto(InvoicingWorkOrderRecord record) {
        InvoicingWorkOrderDto dto = new InvoicingWorkOrderDto();
    
        dto.id = record.id;
        dto.description = record.description;
        dto.date = record.date;
        dto.state = record.state;
        dto.amount = record.amount;
    
        return dto;
    }
    
    public static List<InvoicingWorkOrderDto> toDtoList(List<InvoicingWorkOrderRecord> records) {
        List<InvoicingWorkOrderDto> result = new ArrayList<>();
    
        for (InvoicingWorkOrderRecord record : records)
            result.add(toDto(record));
    
        return result;
    }

}
