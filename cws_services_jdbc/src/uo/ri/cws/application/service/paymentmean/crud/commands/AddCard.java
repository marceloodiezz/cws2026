package uo.ri.cws.application.service.paymentmean.crud.commands;
import java.math.BigDecimal;
import java.time.YearMonth;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.creditcard.CreditCardGateway;
import uo.ri.cws.application.persistence.creditcard.CreditCardGateway.CreditCardRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddCard implements Command<CardDto> {

    private ClientGateway cg = Factories.persistence.forClient();

    private PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();

    private CreditCardGateway ccg = Factories.persistence.forCreditCard();

    private CardDto dto;

    public AddCard(CardDto dto) {
        ArgumentChecks.isNotNull(dto, "The card cannot be null");
        ArgumentChecks.isNotBlank(dto.clientId, "The client id cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.cardNumber, "The card number cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.cardType, "The card type cannot be null or blank");
        ArgumentChecks.isNotNull(dto.cardExpiration, "The expiration date cannot be null");

        this.dto = dto;
    }

    @Override
    public CardDto execute() throws BusinessException {

        BusinessChecks.exists(cg.findById(dto.clientId), "Client does not exist");

        BusinessChecks.doesNotExist(ccg.findByNumber(dto.cardNumber), "Credit card number already exists");

        BusinessChecks.isFalse(dto.cardExpiration.isBefore(YearMonth.now()), "Credit card has expired");

        PaymentMeanRecord pm = new PaymentMeanRecord();
        pm.dtype = "CreditCard";
        pm.clientId = dto.clientId;
        pm.accumulated = BigDecimal.ZERO;

        CreditCardRecord card = new CreditCardRecord();
        card.id = pm.id;
        card.number = dto.cardNumber;
        card.type = dto.cardType;
        card.validThru = dto.cardExpiration.atEndOfMonth();

        pmg.add(pm);
        ccg.add(card);

        dto.id = pm.id;
        dto.version = pm.version;
        dto.accumulatedAmount = pm.accumulated;

        return dto;
    }
}
