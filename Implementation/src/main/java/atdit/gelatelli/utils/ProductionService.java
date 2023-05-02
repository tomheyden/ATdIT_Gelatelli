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

public class ProductionService implements ProductionInterface {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    List<Flavour> productionList = new ArrayList<>();
    static List<FlavourIngredient> flavourIngredientList = getFlavourIngredientTable();
    WarehouseService warehouseService = new WarehouseService();

    public static void produceFlavour(String flavour_name, double amount) {

        String tempingredient = FlavourtoIngredient(flavour_name).toString();
        Double productionAmount = getProductionAmount(flavour_name,amount);

        SortedMap<Date,Double> sortedExpirationList = getExpirationDates(flavour_name);

        String query = "UPDATE warehouse SET amount = amount - ? WHERE flavour_name = ? AND bbd = ?;";

        try (Connection connection = DbConnection.getDbConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            // Set parameters for subquery

            ps.setString(1, flavour_name);

            for (int i = 0; productionAmount > sortedExpirationList.get(i); i++) {

                ps.setDouble(2, productionAmount);
                ps.executeQuery();
                productionAmount =- sortedExpirationList.get(i);
            }

        } catch (SQLException e){}
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

    public static SortedMap<Date, Double> getExpirationDates (String flavour_name) {
        List <Batch> batchTable = getBatchTable();
        SortedMap<Date,Double> resultMap = new TreeMap<Date, Double>();

        for (Batch batch : batchTable) {
            if (batch.ingredient().equalsIgnoreCase(flavour_name))
            resultMap.put((Date) batch.bbd(),batch.amount());
        }
        return resultMap;
    }
}
