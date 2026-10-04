package uo.ri.cws.application.service.invoice.create.commands;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class InvoiceWorkorder {

    private static final String TINVOICES_ADD = 
            "INSERT INTO TInvoices (id, version, number, date, state, subtotal, total, vatRate, vatAmount, "
            + "createdAt, updatedAt, entityState) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
    
    private static final String TINVOICES_FINDNEXTNUMBER = 
            "SELECT MAX(number) FROM TInvoices";
    
    private static final String TWORKORDERS_FINDAMOUNT = 
            "SELECT total_amount FROM TWorkOrders WHERE id = ?";
    
    private static final String TWORKORDERS_FINDID = 
            "SELECT id FROM TWorkOrders WHERE id = ?";
    
    private static final String TWORKORDERS_FINDSTATE = 
            "SELECT state FROM TWorkOrders WHERE id = ?";
    
    private static final String TWORKORDERS_UPDATEINVOICEID = 
            "UPDATE TWorkOrders SET invoice_id = ? WHERE id = ?";
    
    private static final String TWORKORDERS_UPDATESTATE = 
            "UPDATE TWorkOrders SET state = 'INVOICED' WHERE id = ?";
    
    private static final String TWORKORDERS_UPDATEVERSION = 
            "update TWorkOrders set version=version+1 where id = ?";
    
    private static final String TWORKORDERS_UPDATE_TIME_VERSION = 
            "update TWorkOrders set updatedAt = ? where id = ?";
    
    private List<String> workOrderIds;

    public InvoiceWorkorder(List<String> workOrderIds) {
        
        ArgumentChecks.isNotNull(workOrderIds);
        ArgumentChecks.isFalse(workOrderIds.isEmpty());

        for (String id : workOrderIds)
            ArgumentChecks.isNotNull(id);

        this.workOrderIds = workOrderIds;
    }

    public InvoiceDto execute() throws BusinessException {

        try (Connection ignored = Jdbc.createThreadConnection()) {

            BusinessChecks.isTrue(checkWorkOrdersExist(workOrderIds), "some workorder does not exist");

            BusinessChecks.isTrue(checkWorkOrdersApproved(workOrderIds), "some workorder is not approved yet");

            long numberInvoice = generateInvoiceNumber();
            LocalDate dateInvoice = LocalDate.now();

            BigDecimal subtotal = calculateTotalInvoice(workOrderIds);

            double vatRate = vatPercentage(dateInvoice);

            BigDecimal vat = BigDecimal.valueOf(vatPercentage(dateInvoice));

            BigDecimal vatAmount = subtotal.multiply(vat).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);

            BigDecimal total = subtotal.add(vatAmount);

            String idInvoice = createInvoice(
                    numberInvoice,
                    dateInvoice,
                    subtotal,
                    total,
                    vatRate,
                    vatAmount
            );

            linkWorkordersToInvoice(idInvoice, workOrderIds);

            markWorkOrderAsInvoiced(workOrderIds);

            updateVersion(workOrderIds);

            updateTimeVersion(workOrderIds);

            InvoiceDto dto = new InvoiceDto();

            dto.id = idInvoice;
            dto.version = 1L;
            dto.number = numberInvoice;
            dto.date = dateInvoice;
            dto.state = "ISSUED";
            dto.subtotal = subtotal;
            dto.total = total;
            dto.vatPercentage = vat;
            dto.vatAmount = vatAmount;

            return dto;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void updateTimeVersion(List<String> workOrderIds) throws SQLException {
        Connection c = Jdbc.getCurrentConnection();
    
        try (PreparedStatement pst = c
            .prepareStatement(TWORKORDERS_UPDATE_TIME_VERSION)) {
            for (String workOrderID : workOrderIds) {
                pst.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                pst.setString(2, workOrderID);
                pst.executeUpdate();
            }
        }
    }
    
    private void updateVersion(List<String> workOrderIds) throws SQLException {
        Connection c = Jdbc.getCurrentConnection();
    
        try (PreparedStatement pst = c
                .prepareStatement(TWORKORDERS_UPDATEVERSION)) {
            for (String workOrderID : workOrderIds) {
                pst.setString(1, workOrderID);
                pst.executeUpdate();
            }
        }
    }
    
    /*
     * checks whether every work order exist
     */
    private boolean checkWorkOrdersExist(List<String> workOrderIDS)
            throws SQLException {
    
        Connection c = Jdbc.getCurrentConnection();
    
        try (PreparedStatement pst = c.prepareStatement(TWORKORDERS_FINDID)) {
            for (String workOrderID : workOrderIDS) {
                pst.setString(1, workOrderID);
                try (ResultSet rs = pst.executeQuery()) {
                    if (!rs.next())
                        return false; // Si no encuentra la orden de trabajo
                }
            }
        }
        return true;
    }
    
    /*
     * checks whether every work order id is APPROVED
     */
    private boolean checkWorkOrdersApproved(List<String> workOrderIDs)
            throws SQLException {
        Connection connection = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = connection
                .prepareStatement(TWORKORDERS_FINDSTATE)) {
            for (String id : workOrderIDs) {
                pst.setString(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String status = rs.getString("state");
                        if (!"APPROVED".equalsIgnoreCase(status)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    
    /*
     * Generates next invoice number (not to be confused with the inner id)
     */
    private long generateInvoiceNumber() throws SQLException {
        Connection connection = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = connection
                .prepareStatement(TINVOICES_FINDNEXTNUMBER)) {
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1) + 1;
                }
            }
        }
        return 1L; // Si no hay facturas previas, empezamos desde 1
    }
    
    /*
     * Compute total amount of the invoice (as the total of individual work
     * orders' amount
     */
    private BigDecimal calculateTotalInvoice(List<String> workOrderIDs)
            throws SQLException {
        Connection connection = Jdbc.getCurrentConnection();

        BigDecimal total = BigDecimal.ZERO;
        try (PreparedStatement pst = connection
                .prepareStatement(TWORKORDERS_FINDAMOUNT)) {
            for (String id : workOrderIDs) {
                pst.setString(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        total = total.add(rs.getBigDecimal("total_amount"));
                    }
                }
            }
        }
        return total;
    }
    
    /*
     * returns vat percentage
     */
    private double vatPercentage(LocalDate d) {
        return LocalDate.parse("2012-07-01").isBefore(d) ? 21.0 : 18.0;

    }
    
    /*
     * Creates the invoice in the database; returns the id
     */
    private String createInvoice(long numberInvoice,
            LocalDate dateInvoice,
            BigDecimal subtotal,
            BigDecimal total,
            double vatRate,
            BigDecimal vatAmount) throws SQLException {
        Connection connection = Jdbc.getCurrentConnection();

        String idInvoice = UUID.randomUUID().toString();
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        try (PreparedStatement pst = connection
                .prepareStatement(TINVOICES_ADD)) {
            pst.setString(1, idInvoice);
            pst.setLong(2, 1L);
            pst.setLong(3, numberInvoice);
            pst.setDate(4, Date.valueOf(dateInvoice));
            pst.setString(5, "ISSUED");
            pst.setBigDecimal(6, subtotal);
            pst.setBigDecimal(7, total); // Round to 2 decimal places
            pst.setDouble(8, vatRate);
            pst.setBigDecimal(9, vatAmount);
            pst.setTimestamp(10, now);
            pst.setTimestamp(11, now);
            pst.setString(12, "ENABLED");
            pst.executeUpdate();
        }
        return idInvoice;
    }
    
    /*
     * Set the invoice number field in work order table to the invoice number
     * generated
     */
    private void linkWorkordersToInvoice(String invoiceId,
            List<String> workOrderIDs) throws SQLException {
        Connection connection = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = connection
                .prepareStatement(TWORKORDERS_UPDATEINVOICEID)) {
            for (String id : workOrderIDs) {
                pst.setString(1, invoiceId);
                pst.setString(2, id);
                pst.executeUpdate();
            }
        }
    }
    
    /*
     * Sets status to INVOICED for every workorder
     */
    private void markWorkOrderAsInvoiced(List<String> ids) throws SQLException {
        Connection connection = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = connection
                .prepareStatement(TWORKORDERS_UPDATESTATE)) {
            for (String id : ids) {
                pst.setString(1, id);
                pst.executeUpdate();
            }
        }
    }

}
