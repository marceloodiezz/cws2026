package uo.ri.cws.application.persistence.intervention.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.intervention.InterventionGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class InterventionGatewayImpl implements InterventionGateway {

    @Override
    public boolean hasInterventions(String mechanicId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TINTERVENTIONS_COUNT_BY_MECHANIC"))) {
            pst.setString(1, mechanicId);
            
            try (ResultSet rs = pst.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
