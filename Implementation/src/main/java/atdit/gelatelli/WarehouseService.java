package atdit.gelatelli;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            Object[] temp = new Object[3];
            for (Object obj : objarray) {
                temp[i] = obj;
                i++;
            }
            Ingredient ingredient_temp = new Ingredient((String)temp[0],Double.parseDouble(temp[1].toString()),(String) temp[2]);
            ingredients.add(ingredient_temp);
        }
        return ingredients;
    }

    public void updateDBfromWE(String bbd,  double amount, String ingredientName) {

        String sql2 = "INSERT INTO warehouse (bbd, amount, ingredient_name) VALUES ("+ bbd +", "+ amount +", "+ ingredientName +"); ";
        dbConnection.updateDBentry(sql2);
    }



}
