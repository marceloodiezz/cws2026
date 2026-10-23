package uo.ri.cws.application.ui.cashier.paymentmean.action;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.UserInteractionException;
import uo.ri.util.menu.Action;

public class AddCreditCardAction implements Action {
    
    private static final DateTimeFormatter EXPIRATION_FORMAT = DateTimeFormatter.ofPattern("MM/uuuu");
    
    private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();

    @Override
    public void execute() throws BusinessException, UserInteractionException {
        CardDto card = new CardDto();
        
        card.clientId = Console.readString("Client id");
        card.cardNumber = Console.readString("Credit card number");
        card.cardType = Console.readString("Credit card type");
        card.cardExpiration = readExpiration();
        
        CardDto created = service.addCard(card);
        Console.println("Credit card added");
        Console.printf("Id: %s%n", created.id);
        Console.printf("Number: %s%n", created.cardNumber);
        Console.printf("Type: %s%n", created.cardType);
        Console.printf("Expiration: %s%n", created.cardExpiration);
    }
    
    private YearMonth readExpiration() throws UserInteractionException {
        String expiration = Console.readString("Expiration date (MM/yyyy)");
        
        try {
            return YearMonth.parse(expiration, EXPIRATION_FORMAT);
        }
        catch (DateTimeParseException e) {
            throw new UserInteractionException("Invalid expiration date. Expected MM/yyyy", e);
        }
    }

}
