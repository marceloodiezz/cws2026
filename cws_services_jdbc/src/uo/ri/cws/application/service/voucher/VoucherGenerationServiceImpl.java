package uo.ri.cws.application.service.voucher;

import java.util.List;

import uo.ri.cws.application.persistence.util.command.CommandExecutor;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.voucher.commands.FindVouchersByClientNif;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.NotYetImplementedException;

public class VoucherGenerationServiceImpl implements VoucherGenerationService {

    private CommandExecutor executor = new CommandExecutor();

    @Override
    public int[] generateVouchers() throws BusinessException {
        throw new NotYetImplementedException();
    }

    @Override
    public List<VoucherDto> findVouchersByClientNif(String nif) throws BusinessException {
        return executor.execute(new FindVouchersByClientNif(nif));
    }

    @Override
    public List<VoucherSummaryDto> getVoucherSummary() throws BusinessException {
        throw new NotYetImplementedException();
    }

}
