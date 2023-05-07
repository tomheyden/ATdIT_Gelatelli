package atdit.gelatelli.models;

import atdit.gelatelli.utils.*;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The FlavourSingleton class represents a Singleton design pattern for flavours. It provides the ability to retrieve,
 * add, remove, and sort flavours. The Singleton design pattern is used to ensure that only one instance of FlavourSingleton
 * can exist.
 */
public class FlavourSingleton {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static FlavourSingleton instance;
    private List<Flavour> flavours;

    /**
     * Private constructor to prevent creating new instances of FlavourSingleton
     * and initialize the flavours list by reading all flavours from the database.
     */
    private FlavourSingleton() {
        WarehouseService warehouseService = new WarehouseService();
        flavours = readAllFlavours();
    }


    /**
     * This method is used to get the single instance of FlavourSingleton class. If an instance doesn't exist,
     * a new instance will be created.
     *
     * @return the single instance of FlavourSingleton class.
     */
    public static FlavourSingleton getInstance() {
        if (instance == null) {
            instance = new FlavourSingleton();
            logger.debug("New instance of FlavourSingleton created.");
        }
        return instance;
    }

    /**
     * This method is used to get the list of all flavours.
     *
     * @return the list of all flavours.
     */
    public List<Flavour> getFlavours() {
        logger.debug("List of all flavours retrieved.");
        return flavours;
    }


    /**
     * This method is used to add a new flavour to the list of flavours.
     *
     * @param flavour the flavour to be added to the list of flavours.
     */
    public void addFlavour(Flavour flavour) {
        flavours.add(flavour);
        logger.info("New flavour {} added to the list of flavours.", flavour.getFlavourName());
    }


    /**
     * This method is used to remove a flavour from the list of flavours.
     *
     * @param flavour the flavour to be removed from the list of flavours.
     */
    public void removeFlavour(Flavour flavour) {
        if (flavours.remove(flavour)) {
            logger.info("Flavour {} removed from the list of flavours.", flavour.getFlavourName());
        } else {
            logger.warn("Attempt to remove non-existent flavour {} from the list of flavours.", flavour.getFlavourName());
        }
    }

    /**
     * This method is used to sort the flavours by their best before date.
     */
    public void sortByBbd() {
        Collections.sort(flavours);
        logger.info("List of flavours sorted by best before date.");
    }

    /**
     * This method is used to read all flavours from the database and return them as a list of Flavour objects.
     *
     * @return a list of all flavours in the database.
     */
    public static List<Flavour> readAllFlavours() {
        String sql = """
                SELECT * FROM flavour
                """;

        List<Object[]> result = DbConnection.getDbTable(sql);
        List<Flavour> flavours = new ArrayList<>();

        for (Object[] objarray : result) {
            Flavour flavour = new Flavour((String) objarray[0], Double.parseDouble(objarray[1].toString()));
            flavours.add(flavour);
        }
        logger.debug("Retrieved data from database: {}", flavours.size());
        return flavours;
    }

    /**
     * This method is used to increase the sort of a flavour by one.
     *
     * @param flavourName the name of the flavour whose sort is to be increased.
     * @return 0 if the sort of the flavour is successfully increased, 1 if the flavour is not found.
     */
    public int increaseFlavourSort(String flavourName) {
        for (Flavour flavour : flavours) {
            if (flavour.getFlavourName().equals(flavourName)) {
                flavour.increaseSort();
                logger.info("Sort of flavour {} increased by one.", flavourName);
                return 0;
            }
        }
        return 1;
    }
}
