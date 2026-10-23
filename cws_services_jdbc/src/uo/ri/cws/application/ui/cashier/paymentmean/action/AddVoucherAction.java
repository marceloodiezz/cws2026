package uo.ri.cws.application.ui.cashier.paymentmean.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddVoucherAction implements Action {
    
    private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
    
    @Override
    public void execute() throws BusinessException {
        VoucherDto voucher = new VoucherDto();
        
        voucher.clientId = Console.readString("Client id");
        voucher.code = Console.readString("Voucher code");
        voucher.description = Console.readString("Description");
        voucher.availableAmount = Console.readBigDecimal("Available amount: ");
        
        VoucherDto created = service.addVoucher(voucher);
        
        Console.println("Voucher added");
        Console.printf("Id: %s%n", created.id);
        Console.printf("Code: %s%n", created.code);
        Console.printf("Description: %s%n", created.description);
        Console.printf("Available amount: %.2f EUR%n", created.availableAmount);
    }

}
