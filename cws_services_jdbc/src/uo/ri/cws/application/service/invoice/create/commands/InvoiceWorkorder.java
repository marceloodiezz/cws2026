package uo.ri.cws.application.service.invoice.create.commands;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkOrderRecord;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class InvoiceWorkorder implements Command<InvoiceDto> {
    
    private InvoiceGateway ig = Factories.persistence.forInvoice();
    private WorkOrderGateway wg = Factories.persistence.forWorkOrder();
    
    private List<String> workOrderIds;

    public InvoiceWorkorder(List<String> workOrderIds) {
        ArgumentChecks.isNotNull(workOrderIds);
        ArgumentChecks.isFalse(workOrderIds.isEmpty());

        for (String id : workOrderIds)
            ArgumentChecks.isNotNull(id);

        this.workOrderIds = workOrderIds;
    }

    @Override
    public InvoiceDto execute() throws BusinessException {
        
        List<WorkOrderRecord> workOrders = findWorkOrders(workOrderIds);

        BusinessChecks.isTrue(
            checkWorkOrdersApproved(workOrders),
            "some workorder is not approved yet"
        );

        long numberInvoice = ig.getNextInvoiceNumber();

        LocalDate dateInvoice = LocalDate.now();
        
        BigDecimal subtotal = calculateTotalInvoice(workOrders);
        
        double vatRate = vatPercentage(dateInvoice);

        BigDecimal vat = BigDecimal.valueOf(vatRate);

        BigDecimal vatAmount = subtotal.multiply(vat)
                    .divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_EVEN
                    );

        BigDecimal total = subtotal.add(vatAmount);

        InvoiceRecord invoice = new InvoiceRecord();

        invoice.number = numberInvoice;
        invoice.date = dateInvoice;
        invoice.state = "ISSUED";
        invoice.subtotal = subtotal;
        invoice.total = total;
        invoice.vatRate = vatRate;
        invoice.vatAmount = vatAmount;

        ig.add(invoice);

        invoiceWorkOrders(workOrders, invoice.id);

        InvoiceDto dto = new InvoiceDto();

        dto.id = invoice.id;
        dto.version = invoice.version;
        dto.number = invoice.number;
        dto.date = invoice.date;
        dto.state = invoice.state;
        dto.subtotal = invoice.subtotal;
        dto.total = invoice.total;
        dto.vatPercentage = BigDecimal.valueOf(invoice.vatRate);
        dto.vatAmount = invoice.vatAmount;

        return dto;
    }
    
    private List<WorkOrderRecord> findWorkOrders(List<String> ids) throws BusinessException {
        List<WorkOrderRecord> result = new ArrayList<>();
        
        for (String id : ids) {
            Optional<WorkOrderRecord> workOrder = wg.findById(id);
            
            BusinessChecks.exists(workOrder, "some workorder does not exist");
            
            result.add(workOrder.get());
        }
        
        return result;
    }
    
    private boolean checkWorkOrdersApproved(List<WorkOrderRecord> workOrders) {
        for (WorkOrderRecord workOrder : workOrders)
            if (!"APPROVED".equalsIgnoreCase(workOrder.state))
                return false;
        
        return true;
    }
    
    private BigDecimal calculateTotalInvoice(List<WorkOrderRecord> workOrders) {
        BigDecimal total = BigDecimal.ZERO;
        
        for (WorkOrderRecord workOrder : workOrders)
            total = total.add(workOrder.total_amount);
        
        return total;
    }
    
    private double vatPercentage(LocalDate date) {
        return LocalDate.parse("2012-07-01").isBefore(date) ? 21.0 : 18.0;
    }
    
    private void invoiceWorkOrders(List<WorkOrderRecord> workOrders, String invoiceId) {
        LocalDateTime now = LocalDateTime.now();
    
        for (WorkOrderRecord workOrder : workOrders) {
            workOrder.invoice_id = invoiceId;
            workOrder.state = "INVOICED";
            workOrder.updatedAt = now;
            
            wg.update(workOrder);
        }
    }

}
