package uo.ri.cws.application.persistence.client.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

public class ClientRecordAssembler extends BaseRecordAssembler<ClientRecord> {
    
    public ClientRecordAssembler() {
        super(ClientRecord::new);
    }

    @Override
    public ClientRecord toRecord(ResultSet rs) throws SQLException {
        ClientRecord record = super.toRecord(rs);

        record.nif = rs.getString("nif");
        record.name = rs.getString("name");
        record.surname = rs.getString("surname");
        
        record.email = rs.getString("email");
        record.phone = rs.getString("phone");

        record.street = rs.getString("street");
        record.city = rs.getString("city");
        record.country = rs.getString("country");
        record.postalCode = rs.getString("postalCode");

        return record;
    }

}
