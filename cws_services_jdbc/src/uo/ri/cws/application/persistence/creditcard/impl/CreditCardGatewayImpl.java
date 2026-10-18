package uo.ri.cws.application.persistence.creditcard.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.creditcard.CreditCardGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class CreditCardGatewayImpl implements CreditCardGateway {
    
    @Override
    public void add(CreditCardRecord record) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCREDITCARDS_ADD"))) {
            pst.setString(1, record.id);
            pst.setString(2, record.number);
            pst.setString(3, record.type);
            pst.setDate(4, Date.valueOf(record.validThru));
            
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
    
    @Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCREDITCARDS_REMOVE"))) {
            pst.setString(1, id);
            
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
    
    @Override
    public void update(CreditCardRecord record) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCREDITCARDS_UPDATE"))) {
            pst.setString(1, record.number);
            pst.setString(2, record.type);
            pst.setDate(3, Date.valueOf(record.validThru));
            pst.setString(4, record.id);
            
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
    
    @Override
    public Optional<CreditCardRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCREDITCARDS_FINDBYID"))) {
            pst.setString(1, id);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    CreditCardRecord record = new CreditCardRecordAssembler().toRecord(rs);
                    return Optional.of(record);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
    
    @Override
    public List<CreditCardRecord> findAll() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCREDITCARDS_FINDALL"))) {
            try (ResultSet rs = pst.executeQuery()) {
                return new CreditCardRecordAssembler().toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
    
    @Override
    public Optional<CreditCardRecord> findByNumber(String number) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCREDITCARDS_FINDBYNUMBER"))) {
            pst.setString(1, number);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    CreditCardRecord record = new CreditCardRecordAssembler().toRecord(rs);
                    return Optional.of(record);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
    
}
