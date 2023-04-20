package atdit.gelatelli.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.lang.*;


/**

 The Implementation for DB Connection from the UIs

 A method for each table to get Information to display on the User Interface

 */

public class DbConnection {
    private static final Logger log = LoggerFactory.getLogger( MethodHandles.lookup().lookupClass() );


    public static Connection getDbConnection () throws SQLException {

        Properties dbAccessProperties = getDbAccessProperties();

        String url = dbAccessProperties.getProperty( "url" );
        String user = dbAccessProperties.getProperty( "user" );
        String password = dbAccessProperties.getProperty( "password" );

        logMissingParameters( url, user, password );

        Connection connection = getConnection( url, user, password);
        return connection;
    }

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

    public static List getDbTable (String sqlstatement) {
      
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

        } catch( SQLException e ) {
                final String msg = "database access failed";
                log.error(msg, e);
                throw new RuntimeException(msg);
        }
        return finalList;
    }


    public static void updateDBentry (String sqlStatement) {
        String sql2 = sqlStatement;

        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql2)) {

            int rowsInserted = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            final String msg ="Error inserting data: " + e.getMessage();
            //log.error(msg,e)
            throw new RuntimeException( msg );
        }
    }

    private static Properties getDbAccessProperties() {
        Properties dbAccessProperties;

        try( InputStream is = DbConnection.class.getClassLoader().getResourceAsStream( "db.properties" ) ) {
            dbAccessProperties = new Properties();
            dbAccessProperties.load( is );
        }
        catch( IOException | IllegalArgumentException | NullPointerException e ) {
            final String msg = "Loading database connection properties failed";
            log.error( msg, e );
            throw new RuntimeException( msg );
        }
        return dbAccessProperties;
    }

    private static Connection getConnection( String url, String user, String password ) throws SQLException {
        return DriverManager.getConnection( url, user, password );
    }

    public static int getMaxId(String table) {
        int maxId = 0;
        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(id) FROM "+ table);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                maxId = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get the maximum id from the warehouse table", e);
        }
        return maxId;
    }

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
}


