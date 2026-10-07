package uo.ri.cws.application.persistence.mechanic.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class MechanicGatewayImpl implements MechanicGateway {

    @Override
    public void add(MechanicRecord t) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TMECHANICS_ADD"))) {
            pst.setString(1, t.id);
            pst.setString(2, t.nif);
            pst.setString(3, t.name);
            pst.setString(4, t.surname);
            pst.setLong(5, t.version);
            pst.setTimestamp(6, Timestamp.valueOf(t.createdAt));
            pst.setTimestamp(7, Timestamp.valueOf(t.updatedAt));
            pst.setString(8, "ENABLED");    
            
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TMECHANICS_DELETE"))) {
            pst.setString(1, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(MechanicRecord t) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TMECHANICS_UPDATE"))) {
            pst.setString(1, t.name);
            pst.setString(2, t.surname);
            pst.setString(3, t.nif);
            pst.setTimestamp(4, Timestamp.valueOf(t.updatedAt));
            pst.setString(5, t.id);
            
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<MechanicRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TMECHANICS_FINDBYID"))) {
            pst.setString(1, id);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    MechanicRecord record = new MechanicRecordAssembler().toRecord(rs);

                    return Optional.of(record);
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MechanicRecord> findAll() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TMECHANICS_FINDALL"))) {
            try (ResultSet rs = pst.executeQuery();) {
                return new MechanicRecordAssembler().toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<MechanicRecord> findByNif(String nif) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TMECHANICS_FINDBYNIF"))) {
            pst.setString(1, nif);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    MechanicRecord record = new MechanicRecordAssembler().toRecord(rs);

                    return Optional.of(record);
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
