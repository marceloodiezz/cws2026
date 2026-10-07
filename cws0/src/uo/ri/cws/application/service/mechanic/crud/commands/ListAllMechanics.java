package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;

public class ListAllMechanics {
    
    private static final String TMECHANICS_FINDALL = "SELECT ID, NAME, "
        + "SURNAME, NIF, VERSION FROM TMECHANICS";
    
    public List<MechanicDto> execute() {
        List<MechanicDto> mechanics = new ArrayList<>();
        
        try (Connection c = Jdbc.createThreadConnection()) {
            try (PreparedStatement pst = c
                    .prepareStatement(TMECHANICS_FINDALL)) {
                try (ResultSet rs = pst.executeQuery();) {
                    while (rs.next()) {
                        mechanics.add(toDto(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  mechanics;
    }
    
    private MechanicDto toDto(ResultSet rs) throws SQLException {

        MechanicDto dto = new MechanicDto();

        dto.id = rs.getString("id");
        dto.nif = rs.getString("nif");
        dto.name = rs.getString("name");
        dto.surname = rs.getString("surname");
        dto.version = rs.getLong("version");

        return dto;
    }

}
