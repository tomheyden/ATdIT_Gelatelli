package atdit.gelatelli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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

public class DbConnectionTest {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static WarehouseService warehouseService;

    @BeforeAll
    public static void createSingleton() {
        FlavourSingleton.getInstance();
        warehouseService = new WarehouseService();
    }

    @Test
    public void testConnection() throws SQLException {

        log.info("Starting testConnection");
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/eiscafegelatelli", "root", "password");
        Assertions.assertNotNull(connection);
        log.info("Finished testConnection");
    }

    @Test
    public void testreadfromDBtoWE() throws SQLException {

       log.info("Starting testreadfromDBtoWE");

       log.info("Reading ingredients from the database");
       List<Ingredient> actualIngredients = warehouseService.readIngredients();

        List<Ingredient> expectedIngredients = new ArrayList<>();
        expectedIngredients.add(new Ingredient("Cocoa powder",9.99,"kg"));
        expectedIngredients.add(new Ingredient("Cream", 4.03,"l"));
        expectedIngredients.add(new Ingredient("Oreo",9.99,"kg"));
        expectedIngredients.add(new Ingredient("Strawberry",5.02,"kg"));
        expectedIngredients.add(new Ingredient("Vanilla extract",9.97,"l"));

        Assertions.assertEquals(expectedIngredients, actualIngredients);
        log.info("Finished testreadfromDBtoWE");
    }

    @Test
    public void testupdateDB() throws SQLException {
        WarehouseService warehouseService = new WarehouseService();
        warehouseService.updateDBfromWE("2023-08-01",0.1,"Strawberry");
        Assertions.assertNotNull(1);
    }

    @Test
    public void testReadBatches() throws SQLException {
        warehouseService.readFlavoursForSpoilingIngredients();

        List<Flavour> expectedFlavours = new ArrayList<>();
        expectedFlavours.add(new Flavour("Strawberry", 0.12, 2));
        expectedFlavours.add(new Flavour("Chocolate", 0.15, 1));
        expectedFlavours.add(new Flavour("Vanilla", 0.17, 0));
        expectedFlavours.add(new Flavour("Oreo", 0.1, 0));

        Assertions.assertEquals(expectedFlavours,FlavourSingleton.getInstance().getFlavours());

    }
}
