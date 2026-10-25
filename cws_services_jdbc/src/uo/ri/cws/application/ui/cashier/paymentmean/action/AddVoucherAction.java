package uo.ri.cws.application.ui.cashier.paymentmean.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddVoucherAction implements Action {
    
    private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
    
    private ClientCrudService clientService = Factories.service.forClientCrudService();
    
    @Override
    public void execute() throws BusinessException {
        String nif = InputReader.readRequiredString("Client nif");
        
        Optional<ClientDto> client = clientService.findClientByNif(nif);
        
        if (client.isEmpty()) {
            Console.println("Client does not exist");
            return;
        }
        
        VoucherDto voucher = new VoucherDto();
        
        voucher.clientId = client.get().id;
        voucher.code = InputReader.readRequiredString("Voucher code");
        voucher.description = InputReader.readRequiredString("Description");
        voucher.availableAmount = InputReader.readNonNegativeAmount("Available amount");
        
        VoucherDto created = service.addVoucher(voucher);
        
        Console.println("Voucher added");
        Printer.printPaymentMean(created);
    }
    
}
