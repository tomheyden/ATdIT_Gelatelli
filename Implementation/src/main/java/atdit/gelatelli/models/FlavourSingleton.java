package atdit.gelatelli.models;

import atdit.gelatelli.utils.*;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlavourSingleton {
    private static final Logger log = LoggerFactory.getLogger( MethodHandles.lookup().lookupClass() );

    private static FlavourSingleton instance;
    private List<Flavour> flavours;

    private FlavourSingleton() {
        WarehouseService warehouseService = new WarehouseService();
        flavours = readAllFlavours();
    }

    public static FlavourSingleton getInstance() {
        if (instance == null) {
            instance = new FlavourSingleton();
        }
        return instance;
    }

    public List<Flavour> getFlavours() {
        return flavours;
    }

    public void addFlavour(Flavour flavour) {
        flavours.add(flavour);
    }

    public void removeFlavour(Flavour flavour) {
        flavours.remove(flavours);
    }

    public void sortByBbd() {
        Collections.sort(flavours);
    }

    public static List<Flavour> readAllFlavours() {
        String sql = """
                      SELECT * FROM flavour
                      """ ;

        List<Object[]> result = DbConnection.getDbTable(sql);
        List<Flavour> flavours = new ArrayList<>();

        for (Object[] objarray : result) {
            Flavour flavour = new Flavour ((String) objarray[0], Double.parseDouble(objarray[1].toString()));
            flavours.add(flavour);
        }
        log.debug("Retrieved data from database: {}", result);
        return flavours;
    }

    public int increaseFlavourSort(String flavourName) {
        for (Flavour flavour : flavours) {
            if (flavour.getFlavourName().equals(flavourName)) {
                flavour.increaseSort();
                return 0;
            }
        }
        return 1;
    }
}
