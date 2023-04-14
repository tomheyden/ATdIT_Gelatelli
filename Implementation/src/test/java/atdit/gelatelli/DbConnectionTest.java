package atdit.gelatelli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.*;

public class DbConnectionTest{

    @BeforeAll
    public static void createSingleton() {
        FlavourSingleton.getInstance();
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/eiscafegelatelli", "root", "password");
        Assertions.assertNotNull(connection);
    }

    @Test
    public void testreadfromDBtoWE() throws SQLException {
       WarehouseService warehouseService = new WarehouseService();
       List<Ingredient> actualIngredients = warehouseService.readIngredients();

        List<Ingredient> expectedIngredients = new ArrayList<>();
        expectedIngredients.add(new Ingredient("Cocoa powder",9.99,"kg"));
        expectedIngredients.add(new Ingredient("Cream", 4.03,"l"));
        expectedIngredients.add(new Ingredient("Oreo",9.99,"kg"));
        expectedIngredients.add(new Ingredient("Strawberry",5.02,"kg"));
        expectedIngredients.add(new Ingredient("Vanilla extract",9.97,"l"));

       Assertions.assertEquals(expectedIngredients,actualIngredients);
    }

    @Test
    public void testReadBatches() throws SQLException {
        WarehouseService warehouseService = new WarehouseService();
        warehouseService.readFlavoursForSpoilingIngredients();

        List<Flavour> expectedFlavours = new ArrayList<>();
        expectedFlavours.add(new Flavour("Strawberry", 0.12, 2));
        expectedFlavours.add(new Flavour("Chocolate", 0.15, 1));
        expectedFlavours.add(new Flavour("Vanilla", 0.17, 0));
        expectedFlavours.add(new Flavour("Oreo", 0.1, 0));

        Assertions.assertEquals(expectedFlavours,FlavourSingleton.getInstance().getFlavours());

    }
}
