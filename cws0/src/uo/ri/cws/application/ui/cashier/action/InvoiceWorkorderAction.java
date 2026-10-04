package uo.ri.cws.application.ui.cashier.action;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class InvoiceWorkorderAction implements Action {
    
    private InvoicingService service = Factories.service.forCreateInvoiceService();
    
    @Override
    public void execute() throws BusinessException {
        List<String> workOrderIds = new ArrayList<>();

        // Ask the user the work order ids
        do {
            String id = Console.readString("Workorder id");
            workOrderIds.add(id);
        } while (moreWorkOrders());

        InvoiceDto invoice = service.create(workOrderIds);
        
        displayInvoice(
                invoice.number,
                invoice.date,
                invoice.subtotal,
                invoice.vatPercentage,
                invoice.total
        );
    }
    
    private boolean moreWorkOrders() {
        return Console.readString("more work orders? (y/n) ").equalsIgnoreCase("y");
    }

    private void displayInvoice(long numberInvoice,
            LocalDate dateInvoice,
            BigDecimal amount,
            BigDecimal vat,
            BigDecimal total) {

        Console.printf("Invoice number: %d\n", numberInvoice);
        Console.printf("\tDate: %1$td/%1$tm/%1$tY\n", dateInvoice);
        Console.printf("\tAmount: %.2f €\n", amount);
        Console.printf("\tVAT: %.1f %% \n", vat);
        Console.printf("\tTotal (including VAT): %.2f €\n", total);
    }
}
