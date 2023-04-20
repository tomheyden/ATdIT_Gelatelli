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

    public static void produceFlavour(String flavourName) {
        String createSql = "SELECT * FROM flavour_ingredient WHERE flavour_name = " + flavourName;
        List<Object> list= DbConnection.getDbTable(createSql);
        //Flavour flavour = new Flavour()

        double amount = 0.0;
        String ingredient_name = "";

        String updateSql = "UPDATE warehouse SET amount = amount - "+amount+" WHERE ingredient_name = '"+ingredient_name+"'";

        /*
        String ingredientName = rs.getString("ingredient_name");
        int requiredQuantity = rs.getInt("quantity");
        */

        DbConnection.updateDBentry(updateSql);
    }

    @Override
    public void readfromDBtoProduction(Ingredient ingredient) {
        List<Batch> batchlist = new ArrayList<>();

        String sql = "SELECT * FROM warehouse ORDER BY ddb ASC";
        List<Map<String, Object>> rows = DbConnection.getDbTable(sql);
        for (Map<String, Object> row : rows) {
            Batch batch = new Batch(
                    (int) row.get("id"),
                    (Date) row.get("ddb"),
                    (double) row.get("amount"),
                    (String) row.get("ingredient_name")
            );
            batchlist.add(batch);
        }
    }

    @Override
    public void updateDBfromProduction(String flavourName, int amount) {}

}
