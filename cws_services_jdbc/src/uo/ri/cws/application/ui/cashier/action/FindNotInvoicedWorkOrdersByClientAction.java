package uo.ri.cws.application.ui.cashier.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindNotInvoicedWorkOrdersByClientAction implements Action {
    
    private InvoicingService service = Factories.service.forCreateInvoiceService();

    /**
     * Process: 
     * - Ask customer nif 
     * - Display all uncharged workorder (status <> 'INVOICED'). 
     *   For each workorder, display id, vehicle id, date, status, amount 
     *   and description
     */

    @Override
    public void execute() throws BusinessException {
        String nif = InputReader.readRequiredString("Client nif");

        Console.println("\nClient's not invoiced work orders\n");
        
        List<InvoicingWorkOrderDto> workOrders = service.findNotInvoicedWorkOrdersByClientNif(nif);
    
        for (InvoicingWorkOrderDto wo : workOrders) {
            Printer.printInvoicingWorkOrder(wo);
        }

        
    }

}