package atdit.gelatelli.utils;

import atdit.gelatelli.models.Ingredient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger logger = LogManager.getLogger();

    public static Connection conn;

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

        logger.info("Connecting to database with url: {} and user: {}", url, user);
        logMissingParameters(url, user, password);

        Connection connection = getConnection(url, user, password);

        logger.info("Database connection established");
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
            logger.error("Database URL is missing.");
        }
        if (user == null) {
            logger.error("Database username is missing.");
        }
        if (password == null) {
            logger.error("Database password is missing.");
        }
    }

    /**
     * This methods receive an "easy" SQL Statement and returns a List containing the Table with values.
     * <p>
     * With the help of this methods we are able to call it whenever we need any table with values and
     * receive them
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
            logger.error("database access failed", e);
            throw new RuntimeException();
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
            logger.info("{} rows inserted into database", rowsInserted);


        } catch (SQLException e) {
            final String msg = "Error inserting data: " + e.getMessage();
            logger.error(msg,e);
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
            logger.error(msg, e);
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
            final String msg = "Failed to get the maximum id from the " + table + " table";
            logger.error(msg, e);
            throw new RuntimeException(msg);
        }
        return maxId;
    }

    
    /**
     * Returns the unit of the given ingredient by searching through the list of ingredients.
     *
     * This methods is called from both the WarehouseService and ProductionService and is
     * therefore in this Class
     *
     *
     * @return the unit of the ingredient, or null if it is not found
     */
    public static Map<String, String> getUnitfromIngredient() {
        List<Ingredient> ingredientList = Ingredient.readIngredients();
        Map<String, String> result = new HashMap<>();

        for (Ingredient ingredienttemp : ingredientList) {
                result.put(ingredienttemp.getName(),ingredienttemp.getUnit());
            }
        return result;
    }

    /**
     * Checks and deletes entities in the batch table where amount = 0
     *
     */
    public static void deleteAmountZero () {
        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("Delete FROM batch WHERE amount = 0.0")) {

            int rowsInserted = preparedStatement.executeUpdate();
            logger.info("{} rows inserted into database", rowsInserted);


        } catch (SQLException e) {
            final String msg = "Error inserting data: " + e.getMessage();
            logger.error(msg,e);
            throw new RuntimeException(msg);
        }
    }


}


