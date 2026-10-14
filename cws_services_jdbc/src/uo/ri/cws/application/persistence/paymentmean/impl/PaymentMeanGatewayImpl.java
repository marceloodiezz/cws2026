package uo.ri.cws.application.persistence.paymentmean.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class PaymentMeanGatewayImpl implements PaymentMeanGateway {

    @Override
    public void add(PaymentMeanRecord record) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TPAYMENTMEANS_ADD"))) {
            pst.setString(1, record.id);
            pst.setString(2, record.dtype);
            pst.setString(3, record.clientId);
            pst.setBigDecimal(4, record.accumulated);
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

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TPAYMENTMEANS_DELETE"))) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(PaymentMeanRecord record) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TPAYMENTMEANS_UPDATE"))) {
            pst.setString(1, record.dtype);
            pst.setString(2, record.clientId);
            pst.setBigDecimal(3, record.accumulated);
            pst.setTimestamp(4, Timestamp.valueOf(record.updatedAt));
            pst.setString(5, record.entityState);
            pst.setString(6, record.id);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<PaymentMeanRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TPAYMENTMEANS_FINDBYID"))) {
            pst.setString(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    PaymentMeanRecord record = new PaymentMeanRecordAssembler().toRecord(rs);
                    return Optional.of(record);
                }
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<PaymentMeanRecord> findAll() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TPAYMENTMEANS_FINDALL"))) {
            try (ResultSet rs = pst.executeQuery()) {
                return new PaymentMeanRecordAssembler().toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<PaymentMeanRecord> findByClientId(String clientId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TPAYMENTMEANS_FINDBYCLIENTID"))) {
            pst.setString(1, clientId);

            try (ResultSet rs = pst.executeQuery()) {
                return new PaymentMeanRecordAssembler().toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

}
