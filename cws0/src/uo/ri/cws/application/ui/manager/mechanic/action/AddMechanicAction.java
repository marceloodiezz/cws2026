package uo.ri.cws.application.ui.manager.mechanic.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.menu.Action;

public class AddMechanicAction implements Action {
    

    @Override
    public void execute() throws BusinessException {

        // Get info
        String nif = Console.readString("nif");
        String name = Console.readString("Name");
        String surname = Console.readString("Surname");
        String id = UUID.randomUUID().toString();
        long version = 1;
        
        

        // Print result
        Console.println("Mechanic added");
    }

}
