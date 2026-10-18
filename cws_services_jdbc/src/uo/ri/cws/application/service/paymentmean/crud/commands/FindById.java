package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.creditcard.CreditCardGateway;
import uo.ri.cws.application.persistence.creditcard.CreditCardGateway.CreditCardRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.cws.application.persistence.voucher.VoucherGateway.VoucherRecord;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.cws.application.service.paymentmean.crud.PaymentMeanDtoAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindById implements Command<Optional<PaymentMeanDto>> {

    private PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();

    private CreditCardGateway ccg = Factories.persistence.forCreditCard();

    private VoucherGateway vg = Factories.persistence.forVoucher();

    private String id;

    public FindById(String id) {
        ArgumentChecks.isNotNull(id, "The id cannot be null");
        ArgumentChecks.isNotBlank(id, "The id cannot be blank");

        this.id = id;
    }

    @Override
    public Optional<PaymentMeanDto> execute() throws BusinessException {

        Optional<PaymentMeanRecord> record = pmg.findById(id);

        if (record.isEmpty())
            return Optional.empty();

        PaymentMeanRecord pm = record.get();

        switch (pm.dtype) {
            case "Cash":
                return Optional.of(PaymentMeanDtoAssembler.toCashDto(pm));

            case "CreditCard":
                Optional<CreditCardRecord> card = ccg.findById(pm.id);
                return Optional.of(PaymentMeanDtoAssembler.toCardDto(pm, card.orElseThrow()));

            case "Voucher":
                Optional<VoucherRecord> voucher = vg.findById(pm.id);
                return Optional.of(PaymentMeanDtoAssembler.toVoucherDto(pm, voucher.orElseThrow()));

            default:
                throw new IllegalStateException("Unknown payment mean type: " + pm.dtype);
        }
    }

}
