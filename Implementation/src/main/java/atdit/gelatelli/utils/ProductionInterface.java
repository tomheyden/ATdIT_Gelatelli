package atdit.gelatelli.utils;

import atdit.gelatelli.models.Flavour;
import atdit.gelatelli.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public interface ProductionInterface {
    public List<Flavour> productionlist = new ArrayList<>();

    /**
     Method to read from the Database and display the information in the Production UI for the employee
     */

    public void readfromDBtoProduction (Ingredient ingredient);

    /**
     Method to update the DB entries from the Input in the Production UI by the employee
     */
   public void updateDBfromProduction (String flavourName, int amount);
}

