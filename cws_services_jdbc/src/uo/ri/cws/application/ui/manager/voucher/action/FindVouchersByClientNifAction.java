package uo.ri.cws.application.ui.manager.voucher.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindVouchersByClientNifAction implements Action {
    
    private VoucherGenerationService service = Factories.service.forVoucherService();
    
    @Override
    public void execute() throws BusinessException {
        String nif = Console.readString("Client nif");
        
        List<VoucherDto> vouchers = service.findVouchersByClientNif(nif);
        
        if (vouchers.isEmpty()) {
            Console.println("There are no vouchers for this client");
            return;
        }
        
        Console.printf("Vouchers found: %d%n", vouchers.size());
        
        for (VoucherDto voucher : vouchers) {
            Printer.printVoucher(voucher);
        }
        
        for (VoucherSummaryDto summary : service.getVoucherSummary()) {
            if (summary.nif().equals(nif)) {
                Console.println();
                Console.println("Voucher totals");
                Printer.printVoucherSummary(summary);
                break;
            }
        }
    }
    
}
