package atdit.gelatelli.utils;

import atdit.gelatelli.models.Batch;
import atdit.gelatelli.models.Flavour;
import atdit.gelatelli.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * An interface for the production process of Gelatelli.
 */
public interface ProductionInterface {
    /**
     * A list of Flavours produced.
     */
    public List<Flavour> productionlist = new ArrayList<>();

    /**
     * Method to read batches from the Database and display the information in the Production UI for the employee.
     *
     * @return a list of Batch objects from the database
     */
    public static List<Batch> getBatchTable() {
        return null;
    }
}

