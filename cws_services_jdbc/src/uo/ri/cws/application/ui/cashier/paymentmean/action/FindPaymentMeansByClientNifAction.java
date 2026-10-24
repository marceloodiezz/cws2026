package uo.ri.cws.application.ui.cashier.paymentmean.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindPaymentMeansByClientNifAction implements Action {
    
    private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
    
    @Override
    public void execute() throws BusinessException {
        String nif = Console.readString("Client nif");
        
        List<PaymentMeanDto> paymentMeans = service.findPaymentMeansByClientNif(nif);
        
        if (paymentMeans.isEmpty()) {
            Console.println("There are no payment means for this client");
            return;
        }
        
        Console.printf("Payment means found: %d%n", paymentMeans.size());
        
        for (PaymentMeanDto dto : paymentMeans) {
            Printer.printPaymentMean(dto);
        }
    }
    
}
