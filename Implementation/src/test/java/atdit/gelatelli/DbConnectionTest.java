package atdit.gelatelli;

import atdit.gelatelli.models.Ingredient;
import atdit.gelatelli.utils.WarehouseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.*;

/**
 * Unit test for simple App.
 */

public class DbConnectionTest{

    @Test
    public void testConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/eiscafegelatelli", "root", "password");
        Assertions.assertNotNull(connection);
    }
    
    @Test
    public void testreadfromDBtoWE() throws SQLException {
       WarehouseService warehouseService = new WarehouseService();
       List<Ingredient> actualIngredients = warehouseService.readfromDBtoWE(null);

        List<Ingredient> expectedIngredients = new ArrayList<>();
        expectedIngredients.add(new Ingredient("Cocoa powder",9.99,"kg"));
        expectedIngredients.add(new Ingredient("Oreo",9.99,"kg"));
        expectedIngredients.add(new Ingredient("Strawberry",5.02,"kg"));
        expectedIngredients.add(new Ingredient("Vanilla extract",9.97,"l"));

       Assertions.assertEquals(expectedIngredients,actualIngredients);
    }
}
