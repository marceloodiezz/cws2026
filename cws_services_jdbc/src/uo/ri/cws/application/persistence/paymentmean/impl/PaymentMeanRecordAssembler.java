package uo.ri.cws.application.persistence.paymentmean.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

public class PaymentMeanRecordAssembler extends BaseRecordAssembler<PaymentMeanRecord> {
    
    public PaymentMeanRecordAssembler() {
        super(PaymentMeanRecord::new);
    }
    
    @Override
    public PaymentMeanRecord toRecord(ResultSet rs) throws SQLException {
        PaymentMeanRecord record = super.toRecord(rs);
        
        record.dtype = rs.getString("dtype");
        record.clientId = rs.getString("client_id");
        record.accumulated = rs.getBigDecimal("accumulated");
        
        return record;
    }
    
}
