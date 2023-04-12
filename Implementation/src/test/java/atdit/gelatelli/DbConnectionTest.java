package atdit.gelatelli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Unit test for simple App.
 */

public class DbConnectionTest {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test
    public void testConnection() throws SQLException {
        log.info("Starting testConnection");
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/eiscafegelatelli", "root", "CV2*#9c5W8pHgN8");
        Assertions.assertNotNull(connection);
        log.info("Finished testConnection");
    }

    @Test
    public void testreadfromDBtoWE() throws SQLException {
        log.info("Starting testreadfromDBtoWE");
        WarehouseService warehouseService = new WarehouseService();
        log.info("Reading ingredients from the database");
        List<Ingredient> actualIngredients = warehouseService.readfromDBtoWE(null);

        List<Ingredient> expectedIngredients = new ArrayList<>();
        expectedIngredients.add(new Ingredient("Cocoa powder", 9.99, "kg"));
        expectedIngredients.add(new Ingredient("Oreo", 9.99, "kg"));
        expectedIngredients.add(new Ingredient("Strawberry", 5.02, "kg"));
        expectedIngredients.add(new Ingredient("Vanilla extract", 9.97, "l"));

        Assertions.assertEquals(expectedIngredients, actualIngredients);
        log.info("Finished testreadfromDBtoWE");
    }
}
