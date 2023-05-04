package atdit.gelatelli.utils;

import atdit.gelatelli.models.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
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


    public static String errorOfIngredientsamount = "";


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

    public static List<String> getListContent (String flavour) {
        List <String> resultList = new ArrayList<>();

        List <FlavourIngredient> flavourIngredientList = getFlavourIngredientTable();

        for (FlavourIngredient flavourIngredient : flavourIngredientList) {
            if (flavourIngredient.flavour().equals(flavour)) {
                resultList.add(flavourIngredient.amount() + " " + DbConnection.getUnitfromIngredient(flavourIngredient.ingredient()) + " from " + flavourIngredient.ingredient());
            }
        }
        return resultList;
    }

     /**
     * Produces the given flavour in the specified amount.
     *
     * @param inputFlavour the name of the flavour to be produced
     * @param inputAmount       the amount of the flavour to be produced
     */
    public static void produceFlavour(String inputFlavour, Double inputAmount) {
        try (Connection connection = DbConnection.getDbConnection()) {

            Map<String,Double> ingredientsForFlavour = FlavourtoIngredients(inputFlavour);

            for (Map.Entry<String, Double> entry : ingredientsForFlavour.entrySet()) {
                boolean loopbool = true;
                Double UpdatedAmount = inputAmount * getAmountNeededForOne(inputFlavour, entry.getKey());
                while (loopbool) {
                    String querySelect = "SELECT amount,id FROM warehouse WHERE ingredient_name = ? ORDER BY bbd ASC";
                    String queryUpdate = "UPDATE warehouse SET amount = ? WHERE ingredient_name = ? ORDER BY bbd ASC";

                    PreparedStatement stmtSelect = connection.prepareStatement(querySelect, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    stmtSelect.setString(1, entry.getKey());
                    ResultSet rsSelect = stmtSelect.executeQuery();

                    if (rsSelect.next()) {
                        double chargeAmount = rsSelect.getDouble("amount");
                        int chargeId = rsSelect.getInt("id");

                        if (chargeAmount < UpdatedAmount) {
                            String sqlDelete = "DELETE FROM warehouse WHERE id = ?";
                            PreparedStatement stmtDelete = connection.prepareStatement(sqlDelete);
                            stmtDelete.setInt(1, chargeId);
                            int rowsDeleted = stmtDelete.executeUpdate();
                            UpdatedAmount -= chargeAmount;
                            System.out.println(rowsDeleted + " deleted");
                            continue;
                        } else if (chargeAmount >= UpdatedAmount) {
                            PreparedStatement stmtUpdate = connection.prepareStatement(queryUpdate);
                            stmtUpdate.setString(2, entry.getKey());
                            stmtUpdate.setDouble(1, chargeAmount - UpdatedAmount);
                            stmtUpdate.executeUpdate();
                            System.out.println("Update of " + entry.getKey() + " done");
                            break;
                        }
                    } else {
                    }
                }
            }
        } catch (SQLException e) {String errorMessage = e.getMessage();
            System.out.println("Error message: " + errorMessage);}
    }

    /**
     * Returns the Amount of the Ingredient that you need to produce a specific Flavour
     *
     * @param flavour         the name of the flavour to be produced
     * @param ingredient      the ingredient for the flavour
     */
    public static double getAmountNeededForOne(String flavour, String ingredient) {
        try (Connection connection = DbConnection.getDbConnection()) {

            String sql = "SELECT amount FROM flavour_ingredient WHERE ingredient_name = ? AND flavour_name = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, ingredient);
            stmt.setString(2, flavour);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("amount");
            } else {
                throw new IllegalArgumentException("Invalid flavour-ingredient combination: " + flavour + "-" + ingredient);
            }
        } catch(SQLException e) {String errorMessage = e.getMessage();
            System.out.println("Error message: " + errorMessage);}

        return 0.0;
    }

        /**
     * Checks if there are enough ingredient across all batches to produce a specified amount of a Flavour
     *
     * @param inputFlavour    the name of the Flavour to check
     * @param inputAmount     the desired amount to be produced
     * @return true if there are enough Ingredients to produce the desired amount of the Flavour, false otherwise
     */
    public static boolean checkIfEnoughIngredients (String inputFlavour, double inputAmount) {

        try(Connection connection = DbConnection.getDbConnection()) {
            Map<String,Double> ingredientsForFlavour = FlavourtoIngredients(inputFlavour);
            Map<String,Double> ingredientsNotAvailable = new TreeMap<>();

            // Group inventory by ingredient name and sum the amount for each group
            String groupQuery = "SELECT ingredient_name, SUM(amount) AS total_amount FROM warehouse GROUP BY ingredient_name";
            PreparedStatement groupStmt = connection.prepareStatement(groupQuery,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet groupRs = groupStmt.executeQuery();
            Map<String, Double> inventory = new HashMap<>();
            while (groupRs.next()) {
                String ingredient = groupRs.getString("ingredient_name");
                Double amount = groupRs.getDouble("total_amount");
                inventory.put(ingredient, amount);
            }

            /* Comparision of the two List to check if ingredients are available

            - ingredientsForFlavour is a List containing all Ingredients we need to produce the Flavour
            - inventory is a Map containing all the ingredients with a Sum of their amount to check if all the added amount is enough to produce

             */
            Double tempamount;
            for (Map.Entry<String, Double> entry : ingredientsForFlavour.entrySet()) {
                tempamount = inputAmount * getAmountNeededForOne(inputFlavour,entry.getKey());
                if (!inventory.containsKey(entry.getKey())) {
                    ingredientsNotAvailable.put(entry.getKey(),tempamount);
                } else if (inventory.containsKey(entry.getKey()) && inventory.get(entry.getKey())<tempamount) {
                    ingredientsNotAvailable.put(entry.getKey(),inventory.get(entry.getKey())-tempamount);
                }
            }

            /* Check if all ingredients for the flavour are available

            - If yes Then confirm
            - If no then print out error Message containing missing Ingredients with values

             */

            boolean ingredientsAvailable = false;
            if (ingredientsNotAvailable.isEmpty()) {
                System.out.println("All Ingredients are available");
                return true;
            } else {
                StringBuilder sb = new StringBuilder("The following Ingredients are not available --> ");
                for (Map.Entry<String, Double> entry : ingredientsNotAvailable.entrySet()) {
                    sb.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
                }
                errorOfIngredientsamount = sb.deleteCharAt(sb.length() - 1).toString(); // remove the trailing comma
                return false;
            }
        } catch (SQLException e) {}

        return false;
}
