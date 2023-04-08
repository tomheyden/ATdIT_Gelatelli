package atdit.gelatelli;

import java.util.ArrayList;
import java.util.List;

public class ProductionService implements ProductionInterface {
    DbConnection dbConnection = new DbConnection();

    @Override
    public List<Flavour> readfromDBtoProduction(Ingredient ingredient) {
        List<Object[]> result = dbConnection.getDbTable(null, "flavour",null);
        List<Flavour> flavour = new ArrayList<>();

        for (int i = 0; i <= result.size(); i++ ) {
            Flavour flavour_temp = new Flavour((String)result.get(i)[0],(double)result.get(i)[1]);
            flavour.add(flavour_temp);
        }
        return flavour;
    }

    @Override
    public List<Batch> updateDBfromProduction(String flavourName, int amount) {
        return null;
    }
}
