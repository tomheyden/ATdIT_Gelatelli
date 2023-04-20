package atdit.gelatelli.utils;

import java.lang.invoke.MethodHandles;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import atdit.gelatelli.models.*;
import atdit.gelatelli.utils.WarehouseInterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static java.sql.DriverManager.getConnection;

/**
Service for the Warehouse UI (see {@link WarehouseInterface).
 */

public class WarehouseService implements WarehouseInterface {
    private static final Logger log = LoggerFactory.getLogger( MethodHandles.lookup().lookupClass() );
    DbConnection dbConnection = new DbConnection();

    @Override
    public void updateDBfromWE(String bbd,  double amount, String ingredientName) {

        String sql = "INSERT INTO warehouse (id, bbd, amount, ingredient_name) VALUES ("+ (dbConnection.getMaxId("warehouse")+1)+", '"+ Date.valueOf(bbd)+"', "+amount+", '"+ingredientName+"')";
        dbConnection.updateDBentry(sql);
    }
}
