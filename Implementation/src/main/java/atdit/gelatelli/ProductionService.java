package atdit.gelatelli;

import java.util.ArrayList;
import java.util.List;

public class ProductionService implements ProductionInterface {
    DbConnection dbConnection = new DbConnection();
    List<Flavour> productionList = new ArrayList<>();
    List<Ingredient> ingredientlist = new WarehouseService().readIngredients();

    WarehouseService warehouseService = new WarehouseService();

    @Override
    public void readfromDBtoProduction(Ingredient ingredient) {
        /*List<Ingredient> list = warehouseService.readfromDBtoWE(null);


        List<Object[]> result = dbConnection.getDbTable(null, "flavour",null);
        List<Flavour> flavour = new ArrayList<>();

        int i;
        for (Object[] objarray : result) {
            i = 0;
            Object[] temp = new Object[2];
            for (Object obj : objarray) {
                temp[i] = obj;
                i++;
            }
            Flavour flavour_temp = new Flavour((String)temp[0],Double.parseDouble(temp[1].toString()));
            flavour.add(flavour_temp);
        }
        this.productionList = flavour;*/
    }

    @Override
    public void updateDBfromProduction(String flavourName, int amount) {
    }
}
