package atdit.gelatelli;

import java.util.List;

public interface ProductionInterface {

    /**
     Method to read from the Database and display the information in the Warehouse UI for the employee
     */

    List<Flavour> readfromDBtoProduction (Ingredient ingredient);

    /**
     Method to update the DB entries from the Input in the Warehouse UI by the employee
     */
    List<Batch> updateDBfromProduction (String flavourName, int amount);
}

