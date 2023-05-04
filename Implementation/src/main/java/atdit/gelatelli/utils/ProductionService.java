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

public class ProductionService implements ProductionInterface {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    List<Flavour> productionList = new ArrayList<>();
    static List<FlavourIngredient> flavourIngredientList = getFlavourIngredientTable();
    WarehouseService warehouseService = new WarehouseService();

    public static void produceFlavour(String flavour_name, double amount) {

        Map<String,Double> ingredientsForFlavour = FlavourtoIngredients(flavour_name);
        SortedMap<Date,Object[]> sortedExpirationList = getExpirationDates(ingredientsForFlavour);

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
        return batchlist;
    }

    public static Map<String,Double> FlavourtoIngredients(String flavour) {
        List<FlavourIngredient> flavourIngredients = getFlavourIngredientTable();
        Map<String,Double> ingredientsforFlavour = new TreeMap();
        for (FlavourIngredient obj : flavourIngredients) {
            if (obj.flavour().equalsIgnoreCase(flavour)) {
                ingredientsforFlavour.put(obj.ingredient(),obj.amount());
            }
        }
        return ingredientsforFlavour;
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
        return flavourIngredients;
    }

    public static ObservableList<String> getFlavourTable() {
        String sql = """
                      SELECT * FROM flavour
                      """ ;

        List<Object[]> result = DbConnection.getDbTable(sql);
        ObservableList<String> flavourList = FXCollections.observableArrayList();

        for (Object[] objarray : result) {
            String flavour= (String) objarray[0];
            flavourList.add(flavour);
        }
        log.debug("Retrieved data from database: {}", result);
        System.out.println(flavourList);
        return flavourList;
    }

    public static boolean checkifenoughIngredients (String ingredient , double amount) {
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

    public static SortedMap<Date, Object[]> getExpirationDates (Map<String,Double> flavourIngredients) {
        List <Batch> batchTable = getBatchTable();
        SortedMap<Date,Object[]> resultMap = new TreeMap<Date, Object[]>();

        for (Batch batch : batchTable) {
            if (flavourIngredients.containsKey(batch.ingredient()))
                resultMap.put((Date) batch.bbd(),new Object[]{batch.ingredient(),batch.amount()});
        }
        return resultMap;
    }

    public static void produceTest(String inputFlavour, Double inputAmount) {
        try (Connection connection = DbConnection.getDbConnection()) {
            // Get user input for flavour and amount to produce
            String flavour = "Chocolate";
            int amountToProduce = 1;

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
                ingredientsAvailable = true;
            } else {
                StringBuilder sb = new StringBuilder("The following Ingredients are not available --> ");
                for (Map.Entry<String, Double> entry : ingredientsNotAvailable.entrySet()) {
                    sb.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
                }
                String result = sb.deleteCharAt(sb.length() - 1).toString(); // remove the trailing comma
                System.out.println(result);
                return;
            }

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
                        // Handle the case where no rows are returned
                    }
                }
            }

            // Check if all ingredients for the flavour are available
            String query = "SELECT ingredient_name, amount, id FROM warehouse WHERE ingredient_name IN (SELECT ingredient_name FROM flavour_ingredient WHERE flavour_name = ?) ORDER BY bbd ASC";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, flavour);
            ResultSet rs = stmt.executeQuery();
            Map<String, Double> inventoryforFlavour = new HashMap<>();
            while (rs.next()) {
                String ingredient = rs.getString("ingredient_name");
                double amount = rs.getDouble("amount");
                int id = rs.getInt("id");
                inventoryforFlavour.put(ingredient, amount);
            }

            System.out.println(inventoryforFlavour);

        } catch (SQLException e) {String errorMessage = e.getMessage();
            System.out.println("Error message: " + errorMessage);}
    }

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
}
