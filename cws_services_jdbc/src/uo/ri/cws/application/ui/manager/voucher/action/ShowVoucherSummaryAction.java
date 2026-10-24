package uo.ri.cws.application.ui.manager.voucher.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class ShowVoucherSummaryAction implements Action {
    
    private VoucherGenerationService service = Factories.service.forVoucherService();
    
    @Override
    public void execute() throws BusinessException {
        List<VoucherSummaryDto> summaries = service.getVoucherSummary();
        
        if (summaries.isEmpty()) {
            Console.println("There are no vouchers");
            return;
        }
        
        Console.printf("Clients with vouchers: %d%n", summaries.size());
        
        for (VoucherSummaryDto summary : summaries) {
            Console.println();
            Console.printf("Client: %s %s%n", summary.name(), summary.surname());
            Console.printf("NIF: %s%n", summary.nif());
            Console.printf("Issued vouchers: %d%n", summary.issuedQty());
            Console.printf("Total amount: %.2f EUR%n", summary.totalAmount());
            Console.printf("Available amount: %.2f EUR%n", summary.availableAmount());
            Console.printf("Consumed amount: %.2f EUR%n", summary.consumedAmount());
        }
    }
    
}
