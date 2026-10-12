package uo.ri.cws.application.persistence.workorder.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkOrderRecord;

public class WorkOrderRecordAssembler extends BaseRecordAssembler<WorkOrderRecord> {

    public WorkOrderRecordAssembler() {
        super(WorkOrderRecord::new);
    }
    
    @Override
    public WorkOrderRecord toRecord(ResultSet rs) throws SQLException {
        WorkOrderRecord record = super.toRecord(rs);

        record.date = rs.getTimestamp("date").toLocalDateTime();
        record.description = rs.getString("description");
        record.total_amount = rs.getBigDecimal("total_amount");
        record.state = rs.getString("state");
        record.invoice_id = rs.getString("invoice_id");
        record.mechanic_id = rs.getString("mechanic_id");
        record.vehicle_id = rs.getString("vehicle_id");

        return record;
    }

}
