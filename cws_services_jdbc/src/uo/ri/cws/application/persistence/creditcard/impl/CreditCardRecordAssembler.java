package uo.ri.cws.application.persistence.creditcard.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.creditcard.CreditCardGateway.CreditCardRecord;

public class CreditCardRecordAssembler {
    
    public CreditCardRecord toRecord(ResultSet rs) throws SQLException {
        CreditCardRecord record = new CreditCardRecord();
        
        record.id = rs.getString("id");
        record.number = rs.getString("number");
        record.type = rs.getString("type");
        record.validThru = rs.getDate("validThru").toLocalDate();
        
        return record;
    }
    
    public List<CreditCardRecord> toRecordList(ResultSet rs) throws SQLException {
        List<CreditCardRecord> result = new ArrayList<>();
        
        while (rs.next()) {
            result.add(toRecord(rs));
        }
        
        return result;
    }
    
}
