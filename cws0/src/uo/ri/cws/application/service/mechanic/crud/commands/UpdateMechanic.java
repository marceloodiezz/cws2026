package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateMechanic {
    
    private static final String TMECHANICS_UPDATE = 
            "update TMechanics set name = ?, surname = ?, nif = ?, "
            + "version = version + 1, updatedat = ? "
            + "where id = ?";
    
    private MechanicDto dto;
    
    public UpdateMechanic(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto);
        ArgumentChecks.isNotBlank(dto.id);
        ArgumentChecks.isNotBlank(dto.name);
        ArgumentChecks.isNotBlank(dto.surname);
        ArgumentChecks.isNotBlank(dto.nif);
        
        this.dto = dto;
    }

    public void execute() throws BusinessException {
        Optional<MechanicDto> current = new FindById(dto.id).execute();
        BusinessChecks.exists(current, "Mechanic does not exist");
        BusinessChecks.hasVersion(dto.version, current.get().version);
        updateMechanic();
    }
    
    private void updateMechanic() {
        try (Connection c = Jdbc.createThreadConnection()) {
            try (PreparedStatement pst = c
                    .prepareStatement(TMECHANICS_UPDATE)) {
                pst.setString(1, dto.name);
                pst.setString(2, dto.surname);
                pst.setString(3, dto.nif);
                pst.setTimestamp(4, new Timestamp(
                        System.currentTimeMillis()+1));
                pst.setString(5, dto.id);

                pst.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
}
