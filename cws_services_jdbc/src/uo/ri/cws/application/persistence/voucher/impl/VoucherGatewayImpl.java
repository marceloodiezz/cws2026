package uo.ri.cws.application.persistence.voucher.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.util.exception.PersistenceException;

public class VoucherGatewayImpl implements VoucherGateway {

    @Override
    public void add(VoucherRecord record) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TVOUCHERS_ADD"))) {
            pst.setString(1, record.id);
            pst.setString(2, record.code);
            pst.setString(3, record.description);
            pst.setBigDecimal(4, record.available);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TVOUCHERS_REMOVE"))) {
            pst.setString(1, id);
            
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(VoucherRecord record) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TVOUCHERS_UPDATE"))) {
            pst.setString(1, record.code);
            pst.setString(2, record.description);
            pst.setBigDecimal(3, record.available);
            pst.setString(4, record.id);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<VoucherRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TVOUCHERS_FINDBYID"))) {
            pst.setString(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    VoucherRecord record = new VoucherRecordAssembler().toRecord(rs);
                    return Optional.of(record);
                }
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<VoucherRecord> findAll() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TVOUCHERS_FINDALL"))) {
            try (ResultSet rs = pst.executeQuery()) {
                return new VoucherRecordAssembler().toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<VoucherRecord> findByCode(String code) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TVOUCHERS_FINDBYCODE"))) {
            pst.setString(1, code);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    VoucherRecord record = new VoucherRecordAssembler().toRecord(rs);
                    return Optional.of(record);
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

}
