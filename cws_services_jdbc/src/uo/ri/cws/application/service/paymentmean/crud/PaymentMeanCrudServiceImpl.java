package uo.ri.cws.application.service.paymentmean.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.command.CommandExecutor;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.crud.commands.FindById;
import uo.ri.cws.application.service.paymentmean.crud.commands.FindCreditCardByNumber;
import uo.ri.cws.application.service.paymentmean.crud.commands.FindVoucherByCode;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.NotYetImplementedException;

public class PaymentMeanCrudServiceImpl implements PaymentMeanCrudService {

    private CommandExecutor executor = new CommandExecutor();

    @Override
    public CardDto addCard(CardDto card) throws BusinessException {
        throw new NotYetImplementedException();
    }

    @Override
    public VoucherDto addVoucher(VoucherDto voucher) throws BusinessException {
        throw new NotYetImplementedException();
    }

    @Override
    public void deletePaymentMean(String id) throws BusinessException {
        throw new NotYetImplementedException();
    }

    @Override
    public Optional<PaymentMeanDto> findById(String id) throws BusinessException {
        return executor.execute(new FindById(id));
    }

    @Override
    public List<PaymentMeanDto> findPaymentMeansByClientNif(String nif) throws BusinessException {
        throw new NotYetImplementedException();
    }

    @Override
    public Optional<VoucherDto> findVoucherByCode(String code) throws BusinessException {
        return executor.execute(new FindVoucherByCode(code));
    }

    @Override
    public Optional<CardDto> findCreditCardByNumber(String cardNumber) throws BusinessException {
        return executor.execute(new FindCreditCardByNumber(cardNumber));
    }

}
