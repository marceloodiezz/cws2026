package uo.ri.cws.application.ui.manager.voucher.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
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
            Console.println();
            Console.printf("Id: %s%n", voucher.id);
            Console.printf("Code: %s%n", voucher.code);
            Console.printf("Description: %s%n", voucher.description);
            Console.printf("Version: %d%n", voucher.version);
            Console.printf("Available amount: %.2f EUR%n", voucher.availableAmount);
            Console.printf("Accumulated amount: %.2f EUR%n", voucher.accumulatedAmount);
        }
    }
    
}
