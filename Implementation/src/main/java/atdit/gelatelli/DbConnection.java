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

        //logMissingParameters( url, user, password );

        Connection connection = getConnection( url, user, password);
        return connection;
    }

    List getDbTable (String sqlstatement) {

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
                //log.error(msg, e);
                throw new RuntimeException(msg);
        }
        return finalList;
    }

    void updateDBentry (String sqlStatement) {
        String sql2 = sqlStatement;

        try (Connection connection = getDbConnection();) {
             PreparedStatement preparedStatement = connection.prepareStatement(sql2);

             int rowsUpdated = preparedStatement.executeUpdate();
             System.out.println(rowsUpdated);

        } catch (SQLException e) {
            final String msg = "database access failed";
            //log.error(msg, e);
            throw new RuntimeException(msg);
        }
    }

    private Properties getDbAccessProperties() {
        Properties dbAccessProperties;

        try( InputStream is = getClass().getClassLoader().getResourceAsStream( "db.properties" ) ) {
            dbAccessProperties = new Properties();
            dbAccessProperties.load( is );
        }
        catch( IOException | IllegalArgumentException | NullPointerException e ) {
            final String msg = "Loading database connection properties failed";
            // log.error( msg, e );
            throw new RuntimeException( msg );
        }
        return dbAccessProperties;
    }

    private Connection getConnection( String url, String user, String password ) throws SQLException {
        return DriverManager.getConnection( url, user, password );
    }
}
