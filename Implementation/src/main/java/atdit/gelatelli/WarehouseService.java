package atdit.gelatelli;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.sql.DriverManager.getConnection;

/**
Service for the Warehouse UI (see {@link WarehouseInterface).
 */

public class WarehouseService implements WarehouseInterface{
    private static final Logger log = LoggerFactory.getLogger( MethodHandles.lookup().lookupClass() );
    DbConnection dbConnection = new DbConnection();

    @Override
    public List<Ingredient> readfromDBtoWE(String column) {
        log.info("Starting readfromDBtoWE method...");
        List<Object[]> result = dbConnection.getDbTable(null, "ingredient",null);
        List<Ingredient> ingredients = new ArrayList<>();

        int i;

        for (Object[] objarray : result) {
            i = 0;
            Object[] temp = new Object[3];
            for (Object obj : objarray) {
                temp[i] = obj;
                i++;
            }
                try {
            Ingredient ingredient_temp = new Ingredient((String)temp[0],Double.parseDouble(temp[1].toString()),(String) temp[2]);
            ingredients.add(ingredient_temp);
            log.trace("Adding Ingredient object to list: {}", ingredient_temp);
                } catch (Exception e) {
                    log.error("Error converting data to Ingredient objects: {}", e.getMessage());
                }
        }

        /*for (int i = 0; i <= result.size(); i++ ) {

            Ingredient ingredient_temp = new Ingredient((String)result.get(i)[0],(double)result.get(i)[1],(String) result.get(i)[2]);
            ingredients.add(ingredient_temp);
        }*/
        log.debug("Retrieved data from database: {}", result);
        return ingredients;
    }

    @Override
    public List<Batch> updateDBfromWE(String flavourName, int amount) {
        return null;
    }



}
