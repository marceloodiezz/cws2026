package uo.ri.cws.application.service.voucher.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.cws.application.persistence.voucher.VoucherGateway.VoucherRecord;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.cws.application.service.voucher.VoucherDtoAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.PersistenceException;

public class FindVouchersByClientNif implements Command<List<VoucherDto>> {
    
    private ClientGateway cg = Factories.persistence.forClient();
    
    private PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();
    
    private VoucherGateway vg = Factories.persistence.forVoucher();
    
    private String nif;
    
    public FindVouchersByClientNif(String nif) {
        ArgumentChecks.isNotNull(nif, "The nif cannot be null");
        ArgumentChecks.isNotBlank(nif, "The nif cannot be blank");
        
        this.nif = nif;
    }
    
    @Override
    public List<VoucherDto> execute() throws BusinessException {
        List<VoucherDto> result = new ArrayList<>();
        
        Optional<ClientRecord> client = cg.findByNif(nif);
        
        if (client.isEmpty()) {
            return result;
        }
        
        List<PaymentMeanRecord> paymentMeans = pmg.findByClientId(client.get().id);
        
        for (PaymentMeanRecord pm : paymentMeans) {
            if (!"Voucher".equals(pm.dtype)) {
                continue;
            }
            
            VoucherRecord voucher = vg.findById(pm.id)
                    .orElseThrow(() -> new PersistenceException("Voucher not found for payment mean: " + pm.id));
            
            result.add(VoucherDtoAssembler.toDto(pm, voucher));
        }
        
        return result;
    }
    
}
