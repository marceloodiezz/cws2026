package uo.ri.cws.application.persistence.recommendation.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.recommendation.RecommendationGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class RecommendationGatewayImpl implements RecommendationGateway {
    
    @Override
    public void add(RecommendationRecord record) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TRECOMMENDATIONS_ADD"))) {
            pst.setString(1, record.id);
            pst.setString(2, record.recommenderId);
            pst.setString(3, record.recommendedId);
            pst.setBoolean(4, record.usedForVoucher);
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
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TRECOMMENDATIONS_REMOVE"))) {
            pst.setString(1, id);
            
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
    
    @Override
    public void update(RecommendationRecord record) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TRECOMMENDATIONS_UPDATE"))) {
            pst.setString(1, record.recommenderId);
            pst.setString(2, record.recommendedId);
            pst.setBoolean(3, record.usedForVoucher);
            pst.setTimestamp(4, Timestamp.valueOf(record.updatedAt));
            pst.setString(5, record.entityState);
            pst.setString(6, record.id);
            
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
    
    @Override
    public Optional<RecommendationRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TRECOMMENDATIONS_FINDBYID"))) {
            pst.setString(1, id);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new RecommendationRecordAssembler().toRecord(rs));
                }
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
    
    @Override
    public List<RecommendationRecord> findAll() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TRECOMMENDATIONS_FINDALL"))) {
            try (ResultSet rs = pst.executeQuery()) {
                return new RecommendationRecordAssembler().toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
    
    @Override
    public List<RecommendationRecord> findAvailableByRecommender(String recommenderId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TRECOMMENDATIONS_FINDAVAILABLEBYRECOMMENDER"))) {
            pst.setString(1, recommenderId);
            
            try (ResultSet rs = pst.executeQuery()) {
                return new RecommendationRecordAssembler().toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
    
}
