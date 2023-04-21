package atdit.gelatelli.utils;

import atdit.gelatelli.models.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.*;

public class ProductionService implements ProductionInterface {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    List<Flavour> productionList = new ArrayList<>();

    WarehouseService warehouseService = new WarehouseService();

    public static void produceFlavour(String flavour_name, double amount) {

        String updateSql = "UPDATE warehouse SET amount = amount - "+amount+" WHERE ingredient_name = '"+FlavourIngredient.FlavourtoIngredient(flavour_name).toString()+"'";

        DbConnection.updateDBentry(updateSql);
    }

    public static List<Batch> getBatchTable () {
        List<Batch> batchlist = new ArrayList<>();

        String sql = "SELECT * FROM warehouse ORDER BY ddb ASC";
        List<Map<String, Object>> rows = DbConnection.getDbTable(sql);
        for (Map<String, Object> row : rows) {
            Batch batch = new Batch(
                    (int) row.get("id"),
                    (Date) row.get("bbd"),
                    (double) row.get("amount"),
                    (String) row.get("ingredient_name")
            );
            batchlist.add(batch);
        }
        return batchlist;
    }
}
