package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.creditcard.CreditCardGateway;
import uo.ri.cws.application.persistence.creditcard.CreditCardGateway.CreditCardRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.crud.PaymentMeanDtoAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.PersistenceException;

public class FindCreditCardByNumber implements Command<Optional<CardDto>> {
    
    private CreditCardGateway ccg = Factories.persistence.forCreditCard();
    
    private PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();
    
    private String cardNumber;
    
    public FindCreditCardByNumber(String cardNumber) {
        ArgumentChecks.isNotNull(cardNumber, "The card number cannot be null");
        ArgumentChecks.isNotBlank(cardNumber, "The card number cannot be blank");
        
        this.cardNumber = cardNumber;
    }
    
    @Override
    public Optional<CardDto> execute() throws BusinessException {
        Optional<CreditCardRecord> card = ccg.findByNumber(cardNumber);
        
        if (card.isEmpty()) {
            return Optional.empty();
        }
        
        CreditCardRecord cr = card.get();
        PaymentMeanRecord pm = pmg.findById(cr.id)
                .orElseThrow(() -> new PersistenceException("Payment mean not found for card: " + cr.id));
        
        CardDto dto = PaymentMeanDtoAssembler.toCardDto(pm, cr);
        
        return Optional.of(dto);
    }
    
}
