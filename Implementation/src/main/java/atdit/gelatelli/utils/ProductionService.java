package atdit.gelatelli.utils;

import atdit.gelatelli.models.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Date;
import java.util.ArrayList;
import java.util.*;

public class ProductionService implements ProductionInterface {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    List<Flavour> productionList = new ArrayList<>();
    WarehouseService warehouseService = new WarehouseService();

    public static void produceFlavour(String flavour_name, double amount) {

        String updateSql = "UPDATE warehouse SET amount = amount - "+amount+" WHERE ingredient_name = '"+FlavourtoIngredient(flavour_name).toString()+"'";
        DbConnection.updateDBentry(updateSql);

    }

    public static List<Batch> getBatchTable () {
        List<Batch> batchlist = new ArrayList<>();

        String sql = "SELECT * FROM warehouse ORDER BY bbd ASC";
        List<Object []> rows = DbConnection.getDbTable(sql);
        for (Object[] row : rows) {
            Batch batch = new Batch(
                    Integer.parseInt((String)row[0]),
                    Date.valueOf((String)row[1]),
                    Double.parseDouble((String) row[2]),
                    (String) row[3]
            );
            batchlist.add(batch);
        }
        System.out.println(batchlist);
        return batchlist;
    }

    public static String FlavourtoIngredient(String flavour) {
        List<FlavourIngredient> flavourIngredients = getFlavourIngredientTable();

        for (FlavourIngredient obj : flavourIngredients) {
            if (obj.flavour().equalsIgnoreCase(flavour)) {
                return obj.ingredient();
            }
        }
        return null;
    }

    public static List<FlavourIngredient> getFlavourIngredientTable(){
        String sql = """
                      SELECT * FROM flavour_ingredient
                      """ ;

        List<Object[]> result = DbConnection.getDbTable(sql);
        List<FlavourIngredient> flavourIngredients = new ArrayList<>();

        for (Object[] objarray : result) {
            FlavourIngredient flavourIngredientobj = new FlavourIngredient ((String) objarray[0],(String) objarray[1] ,Double.parseDouble(objarray[2].toString()));
            flavourIngredients.add(flavourIngredientobj);
        }
        log.debug("Retrieved data from database: {}", result);
        System.out.println(flavourIngredients);
        return flavourIngredients;
    }
}
