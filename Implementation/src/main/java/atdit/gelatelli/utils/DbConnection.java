package atdit.gelatelli.utils;

import atdit.gelatelli.models.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.util.*;
import java.lang.*;


/**
 * This class provides the methods for establishing the database connection and executing SQL queries
 * related to the Gelatelli application's.
 */

public class DbConnection {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Returns a Connection object for connecting to the Gelatelli database.
     *
     * @return a Connection object representing the connection to the database
     * @throws SQLException if there is a problem with the database connection
     */
    public static Connection getDbConnection() throws SQLException {

        Properties dbAccessProperties = getDbAccessProperties();

        String url = dbAccessProperties.getProperty("url");
        String user = dbAccessProperties.getProperty("user");
        String password = dbAccessProperties.getProperty("password");

        logMissingParameters(url, user, password);

        Connection connection = getConnection(url, user, password);
        return connection;
    }

    /**
     * Logs an error message if any of the database connection parameters are null.
     *
     * @param url      the database URL
     * @param user     the database username
     * @param password the database password
     */
    private static void logMissingParameters(String url, String user, String password) {
        if (url == null) {
            log.error("Database URL is missing.");
        }
        if (user == null) {
            log.error("Database username is missing.");
        }
        if (password == null) {
            log.error("Database password is missing.");
        }
    }

    /**
     * Returns a list of Object arrays representing the result of a SELECT query on the database.
     *
     * @param sqlstatement a String containing the SELECT query to execute
     * @return a List of Object arrays representing the result of the query
     * @throws RuntimeException if there is an error executing the query
     */
    public static List getDbTable(String sqlstatement) {

        String sql1 = sqlstatement;
        List<Object[]> finalList = new ArrayList<>();


        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql1);
             ResultSet dbQueryResult = preparedStatement.executeQuery();) {

            ResultSetMetaData rsmd = dbQueryResult.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (dbQueryResult.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = dbQueryResult.getString(i);
                }
                finalList.add(row);
            }

        } catch (SQLException e) {
            final String msg = "database access failed";
            log.error(msg, e);
            throw new RuntimeException(msg);
        }
        return finalList;
    }

    /**
     * Executes an UPDATE or INSERT query on the database.
     *
     * @param sqlStatement a String containing the UPDATE or INSERT query to execute
     * @throws RuntimeException if there is an error executing the query
     */
    public static void updateDBentry(String sqlStatement) {
        String sql2 = sqlStatement;

        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql2)) {

            int rowsInserted = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            final String msg = "Error inserting data: " + e.getMessage();
            //log.error(msg,e)
            throw new RuntimeException(msg);
        }
    }

    /**
     * Retrieves the database connection properties from the db.properties file.
     *
     * @return the Properties object containing the database access properties.
     * @throws RuntimeException if there is a problem loading the properties file.
     */
    private static Properties getDbAccessProperties() {
        Properties dbAccessProperties;

        try (InputStream is = DbConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            dbAccessProperties = new Properties();
            dbAccessProperties.load(is);
        } catch (IOException | IllegalArgumentException | NullPointerException e) {
            final String msg = "Loading database connection properties failed";
            log.error(msg, e);
            throw new RuntimeException(msg);
        }
        return dbAccessProperties;
    }

    /**
     * Retrieves a database connection from the DriverManager.
     *
     * @param url      the URL of the database to connect to.
     * @param user     the username for the database connection.
     * @param password the password for the database connection.
     * @return a Connection object for the database.
     * @throws SQLException if there is a problem connecting to the database.
     */
    private static Connection getConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Retrieves the maximum id from the specified table.
     *
     * @param table the name of the table to retrieve the maximum id from.
     * @return the maximum id from the specified table.
     * @throws RuntimeException if there is a problem getting the maximum id from the table.
     */
    public static int getMaxId(String table) {
        int maxId = 0;
        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(id) FROM " + table)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                maxId = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get the maximum id from the warehouse table", e);
        }
        return maxId;
    }


    /**
     * Creates a trigger to check for low inventory and insert a new row into the low_inventory table
     * if the amount of a newly inserted ingredient falls below a certain threshold.
     *
     * @param conn the Connection object for the database.
     * @throws SQLException if there is a problem creating the trigger.
     */
    public static void createLowInventoryTrigger(Connection conn) throws SQLException {
        String dbName = "eiscafegelatelli";
        String tableName = "warehouse";
        String ingredientNameColumn = "ingredient_name";
        String amountColumn = "amount";
        int threshold = 5;

        String triggerName = "check_inventory";
        String triggerSql = String.format(
                "CREATE TRIGGER %s AFTER INSERT ON %s " +
                        "FOR EACH ROW " +
                        "BEGIN " +
                        "    IF (SELECT %s FROM %s WHERE %s = NEW.%s) < %d " +
                        "    THEN " +
                        "        INSERT INTO low_inventory (ingredient_name, quantity) " +
                        "        VALUES (NEW.%s, NEW.%s); " +
                        "    END IF; " +
                        "END;",
                triggerName, tableName, amountColumn, tableName,
                ingredientNameColumn, ingredientNameColumn, threshold,
                ingredientNameColumn, amountColumn);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(triggerSql);
        }
    }


    /**
     * Returns the unit of the given ingredient by searching through the list of ingredients.
     *
     * @param ingredient the name of the ingredient to get the unit for
     * @return the unit of the ingredient, or null if it is not found
     */
    public static String getUnitfromIngredient(String ingredient) {
        List<Ingredient> ingredientList = Ingredient.readIngredients();

        for (Ingredient ingredienttemp : ingredientList) {
            if (ingredienttemp.equals(new Ingredient(ingredient, 0.0, null))) {
                return ingredienttemp.getUnit();
            }
        }
        return null;
    }


}


