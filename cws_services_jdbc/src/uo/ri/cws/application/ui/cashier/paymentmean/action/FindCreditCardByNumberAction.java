package uo.ri.cws.application.ui.cashier.paymentmean.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindCreditCardByNumberAction implements Action {
    
    private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
    
    @Override
    public void execute() throws BusinessException {
        String number = Console.readString("Credit card number");
        
        Optional<CardDto> result = service.findCreditCardByNumber(number);
        
        if (result.isEmpty()) {
            Console.println("Credit card not found");
            return;
        }
        
        Printer.printPaymentMean(result.get());
    }
    
}
