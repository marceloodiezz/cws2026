package uo.ri.cws.application.ui.cashier.paymentmean.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
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
        
        CardDto card = result.get();
        
        Console.printf("Id: %s%n", card.id);
        Console.printf("Client id: %s%n", card.clientId);
        Console.printf("Version: %d%n", card.version);
        Console.printf("Accumulated amount: %.2f EUR%n", card.accumulatedAmount);
        Console.printf("Number: %s%n", card.cardNumber);
        Console.printf("Card type: %s%n", card.cardType);
        Console.printf("Expiration: %s%n", card.cardExpiration);
    }
    
}
