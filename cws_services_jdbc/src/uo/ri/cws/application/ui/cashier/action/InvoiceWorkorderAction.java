package uo.ri.cws.application.ui.cashier.action;

import java.util.ArrayList;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class InvoiceWorkorderAction implements Action {
    
    private InvoicingService service = Factories.service.forCreateInvoiceService();
    
    @Override
    public void execute() throws BusinessException {
        List<String> workOrderIds = new ArrayList<>();
        
        do {
            String id = InputReader.readRequiredString("Workorder id");
            workOrderIds.add(id);
        } while (moreWorkOrders());
        
        InvoiceDto invoice = service.create(workOrderIds);
        
        Printer.printInvoice(invoice);
    }
    
    private boolean moreWorkOrders() {
        return InputReader.readYesNo("More work orders? (y/n)");
    }
    
}
