package atdit.gelatelli.utils;

import atdit.gelatelli.models.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.*;

/**
 * This class implements the ProductionInterface and provides the production service methods.
 */
public class ProductionService implements ProductionInterface {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    List<Flavour> productionList = new ArrayList<>();
    static List<FlavourIngredient> flavourIngredientList = getFlavourIngredientTable();
    WarehouseService warehouseService = new WarehouseService();

    /**
     * Produces the given flavour in the specified amount.
     *
     * @param flavour_name the name of the flavour to be produced
     * @param amount       the amount of the flavour to be produced
     */
    public static void produceFlavour(String flavour_name, double amount) {

        Map<String, Double> ingredientsForFlavour = FlavourtoIngredients(flavour_name);
        SortedMap<Date, Double> sortedExpirationList = getExpirationDates(flavour_name);

        System.out.println(ingredientsForFlavour);
        System.out.println(sortedExpirationList);

        /*String query = "UPDATE warehouse SET amount = amount - ? WHERE flavour_name = ? AND bbd = ?;";

        try (Connection connection = DbConnection.getDbConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            // Set parameters for subquery

            ps.setString(1, flavour_name);

            for (int i = 0; productionAmount > sortedExpirationList.get(i); i++) {

                ps.setDouble(2, productionAmount);
                ps.executeQuery();
                productionAmount =- sortedExpirationList.get(i);
            }

        } catch (SQLException e){}*/
    }

    /**
     * Calculates the amount of flavour needed to produce the given amount of the specified flavour.
     *
     * @param flavour the flavour to be produced
     * @param amount  the amount of the flavour to be produced
     * @return the amount of flavour needed to produce the specified amount of the flavour
     */
    public static double getProductionAmount(String flavour, double amount) {
        List<FlavourIngredient> flavourIngredientsList = getFlavourIngredientTable();

        double productionamount = 0;

        for (FlavourIngredient flavourIngredienttemp : flavourIngredientsList) {
            if (flavourIngredienttemp.flavour().equals(flavour)) {
                productionamount = flavourIngredienttemp.amount() * amount;
            }
        }
        return productionamount;
    }

    /**
     * Returns a list of batches ordered by their expiration dates.
     *
     * @return a list of batches ordered by their expiration dates
     */
    public static List<Batch> getBatchTable() {
        List<Batch> batchlist = new ArrayList<>();

        String sql = "SELECT * FROM warehouse ORDER BY bbd ASC";
        List<Object[]> rows = DbConnection.getDbTable(sql);
        for (Object[] row : rows) {
            Batch batch = new Batch(
                    Integer.parseInt((String) row[0]),
                    Date.valueOf((String) row[1]),
                    Double.parseDouble((String) row[2]),
                    (String) row[3]
            );
            batchlist.add(batch);
        }
        return batchlist;
    }

    /**
     * Returns a map of ingredients and their corresponding amounts for a given flavour.
     *
     * @param flavour the name of the flavour
     * @return a map of ingredients and their corresponding amounts for the given flavour
     */
    public static Map<String, Double> FlavourtoIngredients(String flavour) {
        List<FlavourIngredient> flavourIngredients = getFlavourIngredientTable();
        Map<String, Double> ingredientsforFlavour = new TreeMap();
        for (FlavourIngredient obj : flavourIngredients) {
            if (obj.flavour().equalsIgnoreCase(flavour)) {
                ingredientsforFlavour.put(obj.ingredient(), obj.amount());
            }
        }
        return ingredientsforFlavour;
    }

    /**
     * Returns a list of FlavourIngredient objects containing information about all flavour-ingredient relationships.
     *
     * @return a list of FlavourIngredient objects
     */
    public static List<FlavourIngredient> getFlavourIngredientTable() {
        String sql = """
                SELECT * FROM flavour_ingredient
                """;

        List<Object[]> result = DbConnection.getDbTable(sql);
        List<FlavourIngredient> flavourIngredients = new ArrayList<>();

        for (Object[] objarray : result) {
            FlavourIngredient flavourIngredientobj = new FlavourIngredient((String) objarray[0], (String) objarray[1], Double.parseDouble(objarray[2].toString()));
            flavourIngredients.add(flavourIngredientobj);
        }
        log.debug("Retrieved data from database: {}", result);
        return flavourIngredients;
    }

    /**
     * Returns an ObservableList of strings containing the names of all flavours in the database.
     *
     * @return an ObservableList of strings containing the names of all flavours in the database
     */
    public static ObservableList<String> getFlavourTable() {
        String sql = """
                SELECT * FROM flavour
                """;

        List<Object[]> result = DbConnection.getDbTable(sql);
        ObservableList<String> flavourList = FXCollections.observableArrayList();

        for (Object[] objarray : result) {
            String flavour = (String) objarray[0];
            flavourList.add(flavour);
        }
        log.debug("Retrieved data from database: {}", result);
        System.out.println(flavourList);
        return flavourList;
    }

    /**
     * Checks if the current total amount of a given ingredient across all batches is greater than or equal to a specified amount.
     *
     * @param ingredient the name of the ingredient to check
     * @param amount     the desired amount
     * @return true if the current total amount of the given ingredient is greater than or equal to the specified amount, false otherwise
     */
    public static boolean checkifenoughIngredients(String ingredient, double amount) {
        List<Batch> batchlist = getBatchTable();
        double tempamount = 0;
        String flavour;

        for (Batch charge : batchlist) {
            if (charge.ingredient().equals(ingredient)) {
                tempamount += charge.amount();
            }
        }
        return tempamount >= amount;
    }

    /**
     * Returns a list of strings describing the amount of each ingredient required to create the given flavour.
     *
     * @param flavour the name of the flavour
     * @return a list of strings describing the amount of each ingredient required to create the given flavour
     */
    public static List<String> getListContent(String flavour) {
        List<String> resultList = new ArrayList<>();

        List<FlavourIngredient> flavourIngredientList = getFlavourIngredientTable();

        for (FlavourIngredient flavourIngredient : flavourIngredientList) {
            if (flavourIngredient.flavour().equals(flavour)) {
                resultList.add(flavourIngredient.amount() + " " + DbConnection.getUnitfromIngredient(flavourIngredient.ingredient()) + " from " + flavourIngredient.ingredient());
            }
        }
        return resultList;
    }

    /**
     * Returns a sorted map of expiration dates and corresponding amounts of a specific flavour.
     *
     * @param flavour_name the name of the flavour to get the expiration dates for
     * @return a sorted map of expiration dates and corresponding amounts of the specified flavour
     */
    public static SortedMap<Date, Double> getExpirationDates(String flavour_name) {
        List<Batch> batchTable = getBatchTable();
        SortedMap<Date, Double> resultMap = new TreeMap<Date, Double>();

        for (Batch batch : batchTable) {
            if (batch.ingredient().equalsIgnoreCase(flavour_name))
                resultMap.put((Date) batch.bbd(), batch.amount());
        }
        return resultMap;
    }
}
