package uo.ri.cws.application.ui.cashier.paymentmean.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class DeletePaymentMeanAction implements Action {
    
    private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
    
    @Override
    public void execute() throws BusinessException {
        String id = InputReader.readRequiredString("Payment mean id");
        
        service.deletePaymentMean(id);
        
        Console.println("Payment mean deleted");
    }
    
}
