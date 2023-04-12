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
    List<Ingredient> ingredientlist = new WarehouseService().readIngredients();

    WarehouseService warehouseService = new WarehouseService();

    @Override
    public void readfromDBtoProduction(Ingredient ingredient) {}

    @Override
    public void updateDBfromProduction(String flavourName, int amount) {}

}
