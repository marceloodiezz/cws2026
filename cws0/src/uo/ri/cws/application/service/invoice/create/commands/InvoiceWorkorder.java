package uo.ri.cws.application.service.invoice.create.commands;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
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

        BusinessChecks.isTrue(
            checkWorkOrdersExist(workOrderIds),
            "some workorder does not exist"
        );

        BusinessChecks.isTrue(
            checkWorkOrdersApproved(workOrderIds),
            "some workorder is not approved yet"
        );

        long numberInvoice = ig.getNextInvoiceNumber();

        LocalDate dateInvoice = LocalDate.now();

        BigDecimal subtotal = calculateTotalInvoice(workOrderIds);

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

        linkWorkordersToInvoice(invoice.id, workOrderIds);

        markWorkOrdersAsInvoiced(workOrderIds);

        updateVersion(workOrderIds);
        updateTimestamp(workOrderIds);

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
    
    private void updateTimestamp(List<String> ids) {
        for (String id : ids)
            wg.updateTimestamp(id, LocalDateTime.now());
    }
    
    private void updateVersion(List<String> ids) {
        for (String id : ids)
            wg.incrementVersion(id);
    }
    
    /*
     * checks whether every work order exist
     */
    private boolean checkWorkOrdersExist(List<String> workOrderIds) {
        for (String id : workOrderIds)
            if (!wg.exists(id))
                return false;
    
        return true;
    }
    
    /*
     * checks whether every work order id is APPROVED
     */
    private boolean checkWorkOrdersApproved(List<String> workOrderIds) {
        for (String id : workOrderIds) {
            String state = wg.findState(id);
    
            if (!"APPROVED".equalsIgnoreCase(state))
                return false;
        }
    
        return true;
    }
    
    /*
     * Compute total amount of the invoice (as the total of individual work
     * orders' amount
     */
    private BigDecimal calculateTotalInvoice(List<String> ids) {
        BigDecimal total = BigDecimal.ZERO;

        for (String id : ids)
            total = total.add(wg.findAmount(id));
    
        return total;
    }
    
    /*
     * returns vat percentage
     */
    private double vatPercentage(LocalDate date) {
        return LocalDate.parse("2012-07-01").isBefore(date) ? 21.0 : 18.0;
    }
    
    /*
     * Set the invoice number field in work order table to the invoice number
     * generated
     */
    private void linkWorkordersToInvoice(String invoiceId, List<String> ids) {
        for (String id : ids)
            wg.linkToInvoice(id, invoiceId);
    }
    
    /*
     * Sets status to INVOICED for every workorder
     */
    private void markWorkOrdersAsInvoiced(List<String> ids) {
        for (String id : ids)
            wg.markAsInvoiced(id);
    }

}
