package uo.ri.cws.application.ui.util;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import uo.ri.util.console.Console;

public class InputReader {
    
    private static final DateTimeFormatter EXPIRATION_FORMAT = DateTimeFormatter.ofPattern("MM/uuuu");
    
    public static String readRequiredString(String message) {
        while (true) {
            String value = Console.readString(message);
            
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
            
            Console.println("Invalid value. Try again.");
        }
    }
    
    public static BigDecimal readNonNegativeAmount(String message) {
        while (true) {
            BigDecimal amount = Console.readBigDecimal(message);
            
            if (amount != null && amount.signum() >= 0) {
                return amount;
            }
            
            Console.println("Invalid amount. Try again.");
        }
    }
    
    public static YearMonth readExpiration(String message) {
        while (true) {
            String value = readRequiredString(message);
            
            try {
                return YearMonth.parse(value, EXPIRATION_FORMAT);
            } catch (DateTimeParseException e) {
                Console.println("Invalid date. Expected MM/yyyy");
            }
        }
    }
    
    public static boolean readYesNo(String message) {
        while (true) {
            String answer = readRequiredString(message);
            
            if (answer.equalsIgnoreCase("y")) {
                return true;
            }
            
            if (answer.equalsIgnoreCase("n")) {
                return false;
            }
            
            Console.println("Invalid option. Enter y or n.");
        }
    }
    
}
