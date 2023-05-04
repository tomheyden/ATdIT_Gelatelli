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
 * This class provides services for the Warehouse UI (see {@link WarehouseInterface}).
 */

public class WarehouseService implements WarehouseInterface {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    DbConnection dbConnection = new DbConnection();

    /**
     * Updates the warehouse database entries based on the input from the Warehouse UI by the employee.
     *
     * @param bbd            the expiration date of the batch in the format of "yyyy-MM-dd"
     * @param amount         the amount of the ingredient in the batch
     * @param ingredientName the name of the ingredient in the batch
     */
    @Override
    public void updateDBfromWE(String bbd, double amount, String ingredientName) {

        String sql = "INSERT INTO warehouse (id, bbd, amount, ingredient_name) VALUES (" + (dbConnection.getMaxId("warehouse") + 1) + ", '" + Date.valueOf(bbd) + "', " + amount + ", '" + ingredientName + "')";
        dbConnection.updateDBentry(sql);
    }

    /**
     * Returns the list of contents in the warehouse in a user-friendly format.
     *
     * @return a list of strings containing the amount, unit, ingredient name, and expiration date of the batches in the warehouse
     */
    public List<String> getListContent() {
        List<Batch> warehouseList = ProductionService.getBatchTable();
        List<Ingredient> ingredientList = Ingredient.readIngredients();
        List<String> ListContent = new ArrayList<String>();

        for (Batch good : warehouseList) {
            ListContent.add(good.amount() + " " + DbConnection.getUnitfromIngredient(good.ingredient()) + " from " + good.ingredient() + " expiring: " + getbbd(good.ingredient()));
        }
        return ListContent;
    }

    /**
     * Returns a set of ingredients either by their name or unit based on the input column.
     *
     * @param column the column by which the ingredients are sorted (either "Name" or "Unit")
     * @return a set of strings containing the names or units of the ingredients
     */
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

    /**
     * Returns the expiration date of the batch containing the specified ingredient.
     *
     * @param ingredient the name of the ingredient in the batch
     * @return the expiration date of the batch containing the specified ingredient, or null if no such batch exists
     */
    public static Date getbbd(String ingredient) {
        List<Batch> batchList = ProductionService.getBatchTable();

        for (Batch batch : batchList) {
            if (batch.ingredient().equalsIgnoreCase(ingredient)) {
                return (Date) batch.bbd();
            }
        }
        return null;
    }


    /**
     * Inserts a new ingredient batch into the warehouse table of the database.
     *
     * @param batch the ingredient batch to be inserted
     */
    public static void insertIngredient(Batch batch) {

        try (Connection connection = DbConnection.getDbConnection();
             PreparedStatement ps = connection.prepareStatement(" INSERT INTO `warehouse` (`id`,`bbd`, `amount`, `ingredient_name`) VALUES (? ,?, ?, ?)")) {

            ps.setInt(1, (DbConnection.getMaxId("warehouse") + 1));
            ps.setDate(2, (Date) batch.bbd());
            ps.setDouble(3, batch.amount());
            ps.setString(4, batch.ingredient());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
