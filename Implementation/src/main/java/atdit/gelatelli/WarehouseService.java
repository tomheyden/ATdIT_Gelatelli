package atdit.gelatelli;

import java.lang.invoke.MethodHandles;
import java.sql.Date;
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
    public List<Ingredient> readIngredients() {
    
        log.info("Starting readfromDBtoWE method...");
        
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
               
               try {
            Ingredient ingredient_temp = new Ingredient((String)temp[0],Double.parseDouble(temp[1].toString()),(String) temp[2]);
            ingredients.add(ingredient_temp);
            
            log.trace("Adding Ingredient object to list: {}", ingredient_temp);
                } catch (Exception e) {
                    log.error("Error converting data to Ingredient objects: {}", e.getMessage());
                }
        }
        return ingredients;
    }

    public void readFlavoursForSpoilingIngredients() {

         String sqlIngredient = """
                                SELECT ingredient_name FROM warehouse WHERE bbd < DATE_ADD(NOW(), INTERVAL 1 WEEK)
                                """;

         List<Object[]> ingredients = dbConnection.getDbTable(sqlIngredient);

        for (Object[] ingredient : ingredients) {
            String sqlFlavours = "SELECT * FROM flavour WHERE flavour_name IN (SELECT flavour_name FROM flavour_ingredient WHERE ingredient_name = '" + ingredient[0] + "')";
            List<Object[]> flavours = dbConnection.getDbTable(sqlFlavours);

            for (Object[] flavour : flavours) {
                for (Flavour predefinedFlavour : FlavourSingleton.getInstance().getFlavours()) {
                    if (flavour[0].equals(predefinedFlavour.getFlavourName())) {
                        predefinedFlavour.increaseSort();
                    }
                }
            }

        }
        FlavourSingleton.getInstance().sortByBbd();
    }

    public List<Flavour> readAllFlavours() {
        String sql = """
                      SELECT * FROM flavour
                      """ ;

        List<Object[]> result = dbConnection.getDbTable(sql);
        List<Flavour> flavours = new ArrayList<>();


        for (Object[] objarray : result) {
            Flavour flavour = new Flavour ((String) objarray[0], Double.parseDouble(objarray[1].toString()));
            flavours.add(flavour);
        }
        log.debug("Retrieved data from database: {}", result);
        return flavours;
    }

    @Override
    public void updateDBfromWE(String bbd,  double amount, String ingredientName) {

        String sql = "INSERT INTO warehouse (id, bbd, amount, ingredient_name) VALUES ("+ (dbConnection.getMaxId()+1)+", '"+ Date.valueOf(bbd)+"', "+amount+", '"+ingredientName+"')";
        dbConnection.updateDBentry(sql);
    }
}
