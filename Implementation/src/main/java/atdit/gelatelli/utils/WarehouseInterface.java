package atdit.gelatelli.utils;

import atdit.gelatelli.models.Batch;
import atdit.gelatelli.models.Ingredient;

import java.util.*;

public interface WarehouseInterface {

    /**
     Method to update the DB entries from the Input in the Warehouse UI by the employee
     */
    void updateDBfromWE(String bbd,  double amount, String ingredientName);

}
