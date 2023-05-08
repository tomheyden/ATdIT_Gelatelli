package atdit.gelatelli.utils;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import atdit.gelatelli.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class provides services for the Warehouse UI.
 */
public class WarehouseService {

    private static final Logger logger = LogManager.getLogger();

    /**
     * Instance from the DbConnection Class
     */
    DbConnection dbConnection = new DbConnection();

    /**
     * Updates the batch database entries based on the input from the batch UI by the employee.
     *
     * @param bbd            the expiration date of the batch in the format of "yyyy-MM-dd"
     * @param amount         the amount of the ingredient in the batch
     * @param ingredientName the name of the ingredient in the batch
     */

    public void updateDBfromWE(String bbd, double amount, String ingredientName) {
        String sql = "INSERT INTO batch (id, bbd, amount, ingredient_name) VALUES (" + (dbConnection.getMaxId("batch") + 1) + ", '" + Date.valueOf(bbd) + "', " + amount + ", '" + ingredientName + "')";
        dbConnection.updateDBentry(sql);
        logger.info("Database updated with new batch of " + ingredientName + " with expiration date " + bbd + " and amount " + amount);
    }

    /**
     * Returns the list of contents in the batch in the format for the List displayed on the Warehouse UI.
     *
     * @return a list of strings containing the amount, unit, ingredient name, and expiration date of the batches in the batch
     */
    public List<String> getListContent() {
        List<Batch> batchList = ProductionService.getBatchTable();
        Map<String, String> ingredientUnitList = DbConnection.getUnitfromIngredient();
        List<String> ListContent = new ArrayList<String>();

        for (Batch good : batchList) {
            ListContent.add(good.amount() + " " + ingredientUnitList.get(good.ingredient()) + " from " + good.ingredient() + " expiring: " + good.bbd());
        }
        logger.info("batch contents retrieved and returned for List.");
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
        if (column.equalsIgnoreCase("Name")) {
            logger.info("Ingredients returned by name.");
            return nameResults;
        }
        logger.info("Ingredients returned by unit.");
        return unitResults;
    }

    /**
     * Inserts a new ingredient batch into the batch table of the database.
     *
     * @param batch the ingredient batch to be inserted
     */
    public void insertIngredient(Batch batch) {
        logger.info("Inserting new ingredient batch: " + batch);

        try (Connection connection = DbConnection.getDbConnection();
             PreparedStatement ps = connection.prepareStatement(" INSERT INTO `batch` (`id`,`bbd`, `amount`, `ingredient_name`) VALUES (? ,?, ?, ?)")) {

            ps.setInt(1, (DbConnection.getMaxId("batch") + 1));
            ps.setDate(2, (Date) batch.bbd());
            ps.setDouble(3, batch.amount());
            ps.setString(4, batch.ingredient());

            ps.executeUpdate();
            logger.info("New ingredient batch inserted successfully");

        } catch (SQLException e) {
            logger.error("Error while inserting ingredient batch: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
