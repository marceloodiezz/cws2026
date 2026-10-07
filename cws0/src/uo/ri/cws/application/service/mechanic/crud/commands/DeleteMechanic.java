package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteMechanic {
    
    private static final String TMECHANICS_DELETE = "DELETE FROM TMECHANICS "
        + "WHERE ID = ?";
    
    private static final String TWORKORDERS_COUNT_BY_MECHANIC =
            "SELECT COUNT(*) FROM TWORKORDERS WHERE mechanic_id = ?";
    
    private static final String TINTERVENTIONS_COUNT_BY_MECHANIC =
            "SELECT COUNT(*) FROM TINTERVENTIONS WHERE mechanic_id = ?";
    
    private String mechanicId;
    
    public DeleteMechanic(String mechanicId) {
        ArgumentChecks.isNotNull(mechanicId);
        this.mechanicId = mechanicId;
    }
    
    public void execute() throws BusinessException {
        BusinessChecks.exists(new FindById(mechanicId).execute(), "Mechanic does not exist");
        
        try (Connection c = Jdbc.createThreadConnection()) {
            BusinessChecks.isFalse(hasWorkOrders(), "Mechanic has work orders");
            BusinessChecks.isFalse(hasInterventions(), "Mechanic has interventions");

            deleteMechanic();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasWorkOrders() throws SQLException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(TWORKORDERS_COUNT_BY_MECHANIC)) {
            pst.setString(1, mechanicId);
            try (ResultSet rs = pst.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    private boolean hasInterventions() throws SQLException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(TINTERVENTIONS_COUNT_BY_MECHANIC)) {
            pst.setString(1, mechanicId);
            try (ResultSet rs = pst.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    private void deleteMechanic() throws SQLException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(TMECHANICS_DELETE)) {
            pst.setString(1, mechanicId);
            pst.executeUpdate();
        }
    }

}
