package atdit.gelatelli;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;
import java.lang.*;


/**
 The Implementation for DB Connection from the UIs

 A method for each table to get Information to display on the User Interface

 */

public class DbConnection {
    private static final Logger log = LoggerFactory.getLogger( MethodHandles.lookup().lookupClass() );

    public Connection getDbConnection () throws SQLException {

        Properties dbAccessProperties = getDbAccessProperties();

        String url = dbAccessProperties.getProperty( "url" );
        String user = dbAccessProperties.getProperty( "user" );
        String password = dbAccessProperties.getProperty( "password" );

        logMissingParameters( url, user, password );

        Connection connection = getConnection( url, user, password);
        return connection;
    }

    private void logMissingParameters(String url, String user, String password) {
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

    List getDbTable (Object object, String tablename, String columname) {

        final String tablenamefinal = tablename;
        List<Object[]> finalList = new ArrayList<>();

        try (Connection connection = getDbConnection();
             PreparedStatement preparedStatement = prepareStatement(connection,tablenamefinal,columname);
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

    private PreparedStatement prepareStatement( Connection connection, String tablename, String columname ) throws SQLException {

        PreparedStatement result = connection.prepareStatement(
                """
                SELECT * from 
                """ + tablename
        );
        return result;
    }

    private Properties getDbAccessProperties() {
        Properties dbAccessProperties;

        try( InputStream is = getClass().getClassLoader().getResourceAsStream( "db.properties" ) ) {
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

    private Connection getConnection( String url, String user, String password ) throws SQLException {
        return DriverManager.getConnection( url, user, password );
    }
}
