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
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
            logger.error(msg, e);
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
     * @param ingredient the name of the ingredient to get the unit for
     * @return the unit of the ingredient, or null if it is not found
     */
    public static String getUnitfromIngredient(String ingredient) {
        List<Ingredient> ingredientList = Ingredient.readIngredients();

        for (Ingredient ingredienttemp : ingredientList) {
            if (ingredienttemp.equals(new Ingredient(ingredient, 0.0, null))) {
                logger.debug("Unit of ingredient {} found: {}", ingredient, ingredienttemp.getUnit());
                return ingredienttemp.getUnit();
            }
        }
        logger.warn("Unit of ingredient {} not found in the ingredient list", ingredient);
        return null;
    }


}


