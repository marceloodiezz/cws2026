package uo.ri.cws.application.persistence.client.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class ClientGatewayImpl implements ClientGateway {

    @Override
    public void add(ClientRecord record) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCLIENTS_ADD"))) {
            pst.setString(1, record.id);
            pst.setString(2, record.nif);
            pst.setString(3, record.name);
            pst.setString(4, record.surname);
            pst.setString(5, record.email);
            pst.setString(6, record.phone);
            pst.setString(7, record.street);
            pst.setString(8, record.city);
            pst.setString(9, record.country);
            pst.setString(10, record.postalCode);
            pst.setLong(11, record.version);
            pst.setTimestamp(12, Timestamp.valueOf(record.createdAt));
            pst.setTimestamp(13, Timestamp.valueOf(record.updatedAt));
            pst.setString(14, record.entityState);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCLIENTS_DELETE"))) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(ClientRecord record) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCLIENTS_UPDATE"))) {
            pst.setString(1, record.nif);
            pst.setString(2, record.name);
            pst.setString(3, record.surname);
            pst.setString(4, record.email);
            pst.setString(5, record.phone);
            pst.setString(6, record.street);
            pst.setString(7, record.city);
            pst.setString(8, record.country);
            pst.setString(9, record.postalCode);
            pst.setTimestamp(10, Timestamp.valueOf(record.updatedAt));
            pst.setString(11, record.entityState);
            pst.setString(12, record.id);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<ClientRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCLIENTS_FINDBYID"))) {
            pst.setString(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next())
                    return Optional.of(new ClientRecordAssembler().toRecord(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<ClientRecord> findByNif(String nif) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCLIENTS_FINDBYNIF"))) {
            pst.setString(1, nif);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next())
                    return Optional.of(new ClientRecordAssembler().toRecord(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<ClientRecord> findAll() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCLIENTS_FINDALL"))) {
            try (ResultSet rs = pst.executeQuery()) {
                return new ClientRecordAssembler().toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}