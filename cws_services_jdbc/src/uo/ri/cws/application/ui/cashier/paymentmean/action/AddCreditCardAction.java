package uo.ri.cws.application.ui.cashier.paymentmean.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddCreditCardAction implements Action {
    
    private ClientCrudService clientService = Factories.service.forClientCrudService();
    
    private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
    
    @Override
    public void execute() throws BusinessException {
        String nif = InputReader.readRequiredString("Client nif");
        
        Optional<ClientDto> client = clientService.findClientByNif(nif);
        
        if (client.isEmpty()) {
            Console.println("Client does not exist");
            return;
        }
        
        CardDto card = new CardDto();
        
        card.clientId = client.get().id;
        card.cardNumber = InputReader.readRequiredString("Credit card number");
        card.cardType = InputReader.readRequiredString("Credit card type");
        card.cardExpiration = InputReader.readExpiration("Expiration date (MM/yyyy)");
        
        CardDto created = service.addCard(card);
        
        Console.println("Credit card added");
        Printer.printPaymentMean(created);
    }
    
}
