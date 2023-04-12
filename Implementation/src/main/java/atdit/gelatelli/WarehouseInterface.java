package atdit.gelatelli;

import java.util.*;

public interface WarehouseInterface {


    /**
     Method to read from the Database and display the information in the Warehouse UI for the employee
     */

    List<Ingredient> readIngredients();

    /**
     Method to update the DB entries from the Input in the Warehouse UI by the employee
     */
    List<Batch> updateDBfromWE (String flavourName, int amount);
}
