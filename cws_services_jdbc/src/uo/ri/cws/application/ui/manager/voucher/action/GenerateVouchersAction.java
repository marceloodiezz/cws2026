package uo.ri.cws.application.ui.manager.voucher.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class GenerateVouchersAction implements Action {
    
    private VoucherGenerationService service = Factories.service.forVoucherService();
    
    @Override
    public void execute() throws BusinessException {
        int[] generated = service.generateVouchers();
        
        Console.println("Voucher generation finished");
        Console.printf("20 euro vouchers for work orders: %d%n", generated[0]);
        Console.printf("25 euro vouchers for recommendations: %d%n", generated[1]);
        Console.printf("30 euro vouchers for invoices: %d%n", generated[2]);
    }
    
}
