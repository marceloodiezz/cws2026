package uo.ri.cws.application.persistence.workorder.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.util.exception.PersistenceException;

public class WorkOrderGatewayImpl implements WorkOrderGateway {
    
    // Para DeleteMechanic -------------------------

    @Override
    public boolean hasWorkOrders(String mechanicId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_COUNT_BY_MECHANIC"))) {
            pst.setString(1, mechanicId);

            try (ResultSet rs = pst.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    // Para InvoiceWorkorder -----------------------

    @Override
    public boolean exists(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_FINDID"))) {
            pst.setString(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String findState(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_FINDSTATE"))) {
            pst.setString(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next())
                    return rs.getString("state");

                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BigDecimal findAmount(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_FINDAMOUNT"))) {
            pst.setString(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next())
                    return rs.getBigDecimal("total_amount");

                return BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void linkToInvoice(String workOrderId, String invoiceId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_UPDATEINVOICEID"))) {
            pst.setString(1, invoiceId);
            pst.setString(2, workOrderId);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void markAsInvoiced(String workOrderId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_UPDATESTATE"))) {
            pst.setString(1, workOrderId);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void incrementVersion(String workOrderId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_UPDATEVERSION"))) {
            pst.setString(1, workOrderId);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTimestamp(String workOrderId, LocalDateTime timestamp) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_UPDATETIMESTAMP"))) {
            pst.setTimestamp(1, Timestamp.valueOf(timestamp));
            pst.setString(2, workOrderId);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
