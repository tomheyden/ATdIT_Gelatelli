package atdit.gelatelli.utils;

import atdit.gelatelli.models.Batch;
import atdit.gelatelli.models.Ingredient;

import java.util.*;

public interface WarehouseInterface {

    /**
     * Updates the database entries from the input in the Warehouse UI by the employee
     *
     * @param bbd            The batch best before date
     * @param amount         The amount of the ingredient in the batch
     * @param ingredientName The name of the ingredient
     */
    void updateDBfromWE(String bbd, double amount, String ingredientName);

}
