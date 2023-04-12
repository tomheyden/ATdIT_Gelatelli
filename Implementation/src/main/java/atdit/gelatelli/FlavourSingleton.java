package atdit.gelatelli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FlavourSingleton {
    private static FlavourSingleton instance;
    private List<Flavour> flavours;

    private FlavourSingleton() {
        WarehouseService warehouseService = new WarehouseService();
        flavours = warehouseService.readAllFlavours();
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
