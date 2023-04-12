package atdit.gelatelli;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.text.*;

import static java.sql.DriverManager.getConnection;

/**
Service for the Warehouse UI (see {@link WarehouseInterface).
 */

public class WarehouseService implements WarehouseInterface{
    //private static final Logger log = LoggerFactory.getLogger( MethodHandles.lookup().lookupClass() );
    DbConnection dbConnection = new DbConnection();

    @Override
    public List<Ingredient> readIngredients() {
        String sql1 = """
                      SELECT * from ingredient
                      """ ;
        List<Object[]> result = dbConnection.getDbTable(sql1);
        List<Ingredient> ingredients = new ArrayList<>();

        int i;

        for (Object[] objarray : result) {
            i = 0;
            Object[] temp = new Object[result.toArray().length - 1];
            for (Object obj : objarray) {
                temp[i] = obj;
                i++;
            }
            Ingredient ingredient_temp = new Ingredient((String)temp[0],Double.parseDouble(temp[1].toString()),(String) temp[2]);
            ingredients.add(ingredient_temp);
        }
        return ingredients;
    }

    public List<Flavour> readFlavoursForSpoilingIngredients() {
         String sql = """
                      SELECT * FROM flavour WHERE flavour_name IN (SELECT flavour_name FROM flavour_ingredient WHERE ingredient_name IN (SELECT ingredient_name FROM warehouse WHERE bbd < DATE_ADD(NOW(), INTERVAL 1 WEEK)))
                      """ ;

        List<Object[]> result = dbConnection.getDbTable(sql);
        List<Flavour> flavours = new ArrayList<>();


        for (Object[] objarray : result) {
            Flavour flavour = new Flavour ((String) objarray[0], Double.parseDouble(objarray[1].toString()));
            flavours.add(flavour);
        }
        return flavours;
    }

    @Override
    public void updateDBfromWE(String bbd,  double amount, String ingredientName) {

        String sql = "INSERT INTO warehouse (id, bbd, amount, ingredient_name) VALUES ("+ (dbConnection.getMaxId()+1)+", '"+ Date.valueOf(bbd)+"', "+amount+", '"+ingredientName+"')";
        dbConnection.updateDBentry(sql);
    }



}
