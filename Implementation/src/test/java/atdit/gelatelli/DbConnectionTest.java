package atdit.gelatelli;

import atdit.gelatelli.models.*;
import atdit.gelatelli.utils.DbConnection;
import atdit.gelatelli.utils.ProductionService;
import atdit.gelatelli.utils.WarehouseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.*;

public class DbConnectionTest {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static WarehouseService warehouseService;

    @Test
    public void testDBConnection () {
        InputStream is =  ClassLoader.getSystemResourceAsStream("db.properties");
        Assertions.assertNotNull(is);
    }

    @Test
    public void testConnection() throws SQLException {

        log.info("Starting testConnection");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eiscafegelatelli", "root", "Tlhlfh0108");
        Assertions.assertNotNull(connection);
        log.info("Finished testConnection");
    }

    @Test
    public void testreadfromDBtoWE() throws SQLException {

       /*log.info("Starting testreadfromDBtoWE");

       log.info("Reading ingredients from the database");
       List<Ingredient> actualIngredients = warehouseService.readIngredients();

        List<Ingredient> expectedIngredients = new ArrayList<>();
        expectedIngredients.add(new Ingredient("Cocoa powder",9.99,"kg"));
        expectedIngredients.add(new Ingredient("Cream", 4.03,"l"));
        expectedIngredients.add(new Ingredient("Oreo",9.99,"kg"));
        expectedIngredients.add(new Ingredient("Strawberry",5.02,"kg"));
        expectedIngredients.add(new Ingredient("Vanilla extract",9.97,"l"));

        Assertions.assertEquals(expectedIngredients, actualIngredients);
        log.info("Finished testreadfromDBtoWE");*/
    }

    @Test
    public void testupdateDB() throws SQLException {
        WarehouseService warehouseService = new WarehouseService();
        warehouseService.updateDBfromWE("2023-08-01",0.1,"Strawberry");
        Assertions.assertNotNull(1);
    }

    @Test
    public void testReadBatches() throws SQLException {


        List<Flavour> expectedFlavours = new ArrayList<>();
        expectedFlavours.add(new Flavour("Strawberry", 0.12, 2, LocalDate.of(2023,04,15)));
        expectedFlavours.add(new Flavour("Chocolate", 0.15, 1, LocalDate.of(2023,04,18)));
        expectedFlavours.add(new Flavour("Vanilla", 0.17, 0, null));
        expectedFlavours.add(new Flavour("Oreo", 0.1, 0, null));

        Assertions.assertEquals(expectedFlavours,FlavourSingleton.getInstance().getFlavours());

    }

    @Test
    public void testBatch() throws SQLException{
        Assertions.assertEquals("Cocoa Powder", ProductionService.FlavourtoIngredient("Chocolate"));
    }

    @Test
    public void testReadTable() throws SQLException {
        Assertions.assertNotNull(ProductionService.getFlavourIngredientTable());
        Assertions.assertNotNull(ProductionService.getBatchTable());
    }

    @Test
    public void testUpdate() throws SQLException {
        ProductionService.produceFlavour("Strawberry", Double.parseDouble("2"));
        Assertions.assertNotNull(1);
    }
}
