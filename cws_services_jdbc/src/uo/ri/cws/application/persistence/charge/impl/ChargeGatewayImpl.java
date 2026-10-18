package uo.ri.cws.application.persistence.charge.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.charge.ChargeGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class ChargeGatewayImpl implements ChargeGateway {

    @Override
    public int countByPaymentMeanId(String paymentMeanId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCHARGES_COUNTBYPAYMENTMEANID"))) {
            pst.setString(1, paymentMeanId);

            try (ResultSet rs = pst.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void add(ChargeRecord record) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCHARGES_ADD"))) {
            pst.setString(1, record.id);
            pst.setBigDecimal(2, record.charge);
            pst.setString(3, record.invoiceId);
            pst.setString(4, record.paymentMeanId);
            pst.setLong(5, record.version);
            pst.setTimestamp(6, Timestamp.valueOf(record.createdAt));
            pst.setTimestamp(7, Timestamp.valueOf(record.updatedAt));
            pst.setString(8, record.entityState);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCHARGES_REMOVE"))) {
            pst.setString(1, id);
            
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(ChargeRecord record) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCHARGES_UPDATE"))) {
            pst.setBigDecimal(1, record.charge);
            pst.setString(2, record.invoiceId);
            pst.setString(3, record.paymentMeanId);
            pst.setTimestamp(4, Timestamp.valueOf(record.updatedAt));
            pst.setString(5, record.entityState);
            pst.setString(6, record.id);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<ChargeRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCHARGES_FINDBYID"))) {
            pst.setString(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next())
                    return Optional.of(new ChargeRecordAssembler().toRecord(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<ChargeRecord> findAll() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCHARGES_FINDALL"))) {
            try (ResultSet rs = pst.executeQuery()) {
                return new ChargeRecordAssembler().toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
