package uo.ri.cws.application.service.invoice.create.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.InvoicingWorkOrderRecord;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.invoice.create.InvoicingWorkOrderDtoAssembler;
import uo.ri.util.assertion.ArgumentChecks;

public class FindNotInvoicedWorkOrdersByClient implements Command<List<InvoicingWorkOrderDto>> {
    
    private WorkOrderGateway wg = Factories.persistence.forWorkOrder();
    
    private String nif;
    
    public FindNotInvoicedWorkOrdersByClient(String nif) {
        ArgumentChecks.isNotNull(nif);
        ArgumentChecks.isNotBlank(nif);

        this.nif = nif;
    }
    
    @Override
    public List<InvoicingWorkOrderDto> execute() {
        List<InvoicingWorkOrderRecord> records = wg.findNotInvoicedByClientNif(nif);

        return InvoicingWorkOrderDtoAssembler.toDtoList(records);
    }

}
