package uo.ri.cws.application.ui.cashier.paymentmean.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
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
        
        PaymentMeanDto dto = result.get();
        
        Console.printf("Id: %s%n", dto.id);
        Console.printf("Client id: %s%n", dto.clientId);
        Console.printf("Version: %d%n", dto.version);
        Console.printf("Accumulated amount: %.2f EUR%n", dto.accumulatedAmount);
        
        if (dto instanceof CardDto) {
            CardDto card = (CardDto) dto;
            
            Console.println("Type: Credit card");
            Console.printf("Number: %s%n", card.cardNumber);
            Console.printf("Card type: %s%n", card.cardType);
            Console.printf("Expiration: %s%n", card.cardExpiration);
        }
        else if (dto instanceof VoucherDto) {
            VoucherDto voucher = (VoucherDto) dto;
            
            Console.println("Type: Voucher");
            Console.printf("Code: %s%n", voucher.code);
            Console.printf("Description: %s%n", voucher.description);
            Console.printf("Available amount: %.2f EUR%n", voucher.availableAmount);
        }
        else {
            Console.println("Type: Cash");
        }
    }
    
}
