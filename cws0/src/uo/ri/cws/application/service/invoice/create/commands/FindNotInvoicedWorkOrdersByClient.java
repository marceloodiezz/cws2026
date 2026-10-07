package uo.ri.cws.application.service.invoice.create.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.util.assertion.ArgumentChecks;

public class FindNotInvoicedWorkOrdersByClient {
    
    private static final String TWORKORDERS_FINDNOTINVOICED = 
            "select a.id, a.description, a.date, a.state, a.total_amount"
            + " from TWorkOrders as a, TVehicles as v, TClients as c"
            + " where a.vehicle_id = v.id" + " and v.client_id = c.id and "
            + "state like 'APPROVED' and nif like ?";
    
    private String nif;
    
    public FindNotInvoicedWorkOrdersByClient(String nif) {
        ArgumentChecks.isNotNull(nif);
        ArgumentChecks.isNotBlank(nif);

        this.nif = nif;
    }
    
    public List<InvoicingWorkOrderDto> execute() {
        
        List<InvoicingWorkOrderDto> result = new ArrayList<>();
    
        try (Connection c = Jdbc.createThreadConnection()) {
            try (PreparedStatement pst = c
                    .prepareStatement(TWORKORDERS_FINDNOTINVOICED)) {
                pst.setString(1, nif);
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        InvoicingWorkOrderDto dto = new InvoicingWorkOrderDto();
                        dto.id = rs.getString(1);
                        dto.description = rs.getString(2);
                        dto.date = rs.getTimestamp(3).toLocalDateTime();
                        dto.state = rs.getString(4);
                        dto.amount = rs.getBigDecimal(5);
    
                        result.add(dto);
                    }
                }
            }
    
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    
        return result;
        
    }

}
