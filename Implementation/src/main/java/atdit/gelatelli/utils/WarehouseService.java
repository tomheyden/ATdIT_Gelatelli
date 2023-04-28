package atdit.gelatelli.utils;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import atdit.gelatelli.models.*;
import atdit.gelatelli.utils.WarehouseInterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static java.sql.DriverManager.getConnection;

/**
Service for the Warehouse UI (see {@link WarehouseInterface).
 */

public class WarehouseService implements WarehouseInterface {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    DbConnection dbConnection = new DbConnection();

    @Override
    public void updateDBfromWE(String bbd, double amount, String ingredientName) {

        String sql = "INSERT INTO warehouse (id, bbd, amount, ingredient_name) VALUES (" + (dbConnection.getMaxId("warehouse") + 1) + ", '" + Date.valueOf(bbd) + "', " + amount + ", '" + ingredientName + "')";
        dbConnection.updateDBentry(sql);
    }

    public List<String> getListContent() {
        List<Batch> warehouseList = ProductionService.getBatchTable();
        List<Ingredient> ingredientList = Ingredient.readIngredients();
        List<String> ListContent = new ArrayList<String>();

        for (Batch good : warehouseList) {
            ListContent.add(good.amount() + " " + DbConnection.getUnitfromIngredient(good.ingredient()) + " from " + good.ingredient() + " expiring: " + getbbd(good.ingredient()));
        }
        return ListContent;
    }

    public Set<String> getIngredients(String column) {
        List<Ingredient> tempIngredient = Ingredient.readIngredients();
        Set<String> nameResults = new HashSet<>();
        Set<String> unitResults = new HashSet<>();

        for (Ingredient ingredient : tempIngredient) {
            nameResults.add(ingredient.getName());
            unitResults.add(ingredient.getUnit());
        }
        if (column.equalsIgnoreCase("Name"))
            return nameResults;
        return unitResults;
    }

    public static Date getbbd(String ingredient) {
        List<Batch > batchList = ProductionService.getBatchTable();

        for (Batch batch : batchList) {
            if (batch.ingredient().equalsIgnoreCase(ingredient)) {
                return (Date) batch.bbd();
            }
        }
        return null;
    }

    public static void insertIngredient (Batch batch) {

        try (Connection connection = DbConnection.getDbConnection();
             PreparedStatement ps = connection.prepareStatement(" INSERT INTO `warehouse` (`id`,`bbd`, `amount`, `ingredient_name`) VALUES (? ,?, ?, ?)")) {

            ps.setInt(1,(DbConnection.getMaxId("warehouse")+1));
            ps.setDate(2,(Date) batch.bbd());
            ps.setDouble(3,batch.amount());
            ps.setString(4,batch.ingredient());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
