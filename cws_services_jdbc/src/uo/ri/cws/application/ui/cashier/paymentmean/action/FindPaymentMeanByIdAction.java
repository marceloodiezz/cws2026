package uo.ri.cws.application.ui.cashier.paymentmean.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindPaymentMeanByIdAction implements Action {
    
    private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
    
    @Override
    public void execute() throws BusinessException {
        String id = Console.readString("Payment mean id");
        
        Optional<PaymentMeanDto> result = service.findById(id);
        
        if (result.isEmpty()) {
            Console.println("Payment mean not found");
            return;
        }
        
        Printer.printPaymentMean(result.get());
    }
    
}
