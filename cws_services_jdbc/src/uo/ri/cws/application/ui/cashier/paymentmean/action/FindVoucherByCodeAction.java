package uo.ri.cws.application.ui.cashier.paymentmean.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindVoucherByCodeAction implements Action {
    
    private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
    
    @Override
    public void execute() throws BusinessException {
        String code = Console.readString("Voucher code");
        
        Optional<VoucherDto> result = service.findVoucherByCode(code);
        
        if (result.isEmpty()) {
            Console.println("Voucher not found");
            return;
        }
        
        VoucherDto voucher = result.get();
        
        Console.printf("Id: %s%n", voucher.id);
        Console.printf("Client id: %s%n", voucher.clientId);
        Console.printf("Version: %d%n", voucher.version);
        Console.printf("Accumulated amount: %.2f EUR%n", voucher.accumulatedAmount);
        Console.printf("Code: %s%n", voucher.code);
        Console.printf("Description: %s%n", voucher.description);
        Console.printf("Available amount: %.2f EUR%n", voucher.availableAmount);
    }
    
}
