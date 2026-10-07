package uo.ri.cws.application.persistence.workorder.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.InvoicingWorkOrderRecord;

public class InvoicingWorkOrderRecordAssembler {
    
    public InvoicingWorkOrderRecord toRecord(ResultSet rs) throws SQLException {
        InvoicingWorkOrderRecord record = new InvoicingWorkOrderRecord();

        record.id = rs.getString("id");
        record.description = rs.getString("description");
        record.date = rs.getTimestamp("date").toLocalDateTime();
        record.state = rs.getString("state");
        record.amount = rs.getBigDecimal("total_amount");
        
        return record;
    }
    
    public List<InvoicingWorkOrderRecord> toRecordList(ResultSet rs) throws SQLException {
        List<InvoicingWorkOrderRecord> result = new ArrayList<>();
    
        while (rs.next())
            result.add(toRecord(rs));
    
        return result;
    }

}
