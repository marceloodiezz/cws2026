package uo.ri.cws.application.persistence.invoice.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class InvoiceGatewayImpl implements InvoiceGateway {

    @Override
    public long getNextInvoiceNumber() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TINVOICES_FINDNEXTNUMBER"))) {

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next())
                    return rs.getLong(1) + 1;
            }

            return 1L;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void add(InvoiceRecord invoice) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TINVOICES_ADD"))) {

            pst.setString(1, invoice.id);
            pst.setLong(2, invoice.version);
            pst.setLong(3, invoice.number);
            pst.setDate(4, Date.valueOf(invoice.date));
            pst.setString(5, invoice.state);
            pst.setBigDecimal(6, invoice.subtotal);
            pst.setBigDecimal(7, invoice.total);
            pst.setDouble(8, invoice.vatRate);
            pst.setBigDecimal(9, invoice.vatAmount);
            pst.setTimestamp(10, Timestamp.valueOf(invoice.createdAt));
            pst.setTimestamp(11, Timestamp.valueOf(invoice.updatedAt));
            pst.setString(12, invoice.entityState);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(String id) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(InvoiceRecord t) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<InvoiceRecord> findById(String id) throws PersistenceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<InvoiceRecord> findAll() throws PersistenceException {
        throw new UnsupportedOperationException();
    }

}
