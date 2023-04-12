package atdit.gelatelli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

public class ProductionService implements ProductionInterface {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    DbConnection dbConnection = new DbConnection();
    List<Flavour> productionList = new ArrayList<>();

    @Override
    public void readfromDBtoProduction(Ingredient ingredient) {
        log.info("Starting readfromDBtoProduction method...");
        List<Object[]> result = dbConnection.getDbTable(null, "flavour", null);
        List<Flavour> flavour = new ArrayList<>();

        int i;
        for (Object[] objarray : result) {
            i = 0;
            Object[] temp = new Object[2];
            for (Object obj : objarray) {
                temp[i] = obj;
                i++;
            }
            try {
                Flavour flavour_temp = new Flavour((String) temp[0], Double.parseDouble(temp[1].toString()));
                flavour.add(flavour_temp);
                log.trace("Adding Flavour object to list: {}", flavour_temp);
            } catch (Exception e) {
                log.error("Error converting data to Flavour objects: {}", e.getMessage());
            }
        }

        this.productionList = flavour;
        log.debug("Retrieved data from database: {}", result);
    }

    @Override
    public void updateDBfromProduction(String flavourName, int amount) {
    }

    public void sortList() {
        log.info("Starting sortList method...");
        List<Flavour> tempList = this.productionList;

        //tempList.sort(ProductionListComparator);
        log.trace("Sorted productionList: {}", this.productionList);
    }
}
