package uo.ri.cws.application.persistence.voucher.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.voucher.VoucherGateway.VoucherRecord;

public class VoucherRecordAssembler {
    
    public VoucherRecord toRecord(ResultSet rs) throws SQLException {
        VoucherRecord record = new VoucherRecord();
        
        record.id = rs.getString("id");
        record.code = rs.getString("code");
        record.description = rs.getString("description");
        record.available = rs.getBigDecimal("available");
        
        return record;
    }
    
    public List<VoucherRecord> toRecordList(ResultSet rs) throws SQLException {
        List<VoucherRecord> result = new ArrayList<>();
        
        while (rs.next()) {
            result.add(toRecord(rs));
        }
        
        return result;
    }
    
}
