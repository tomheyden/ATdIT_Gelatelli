package atdit.gelatelli.models;

import atdit.gelatelli.utils.DbConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public record FlavourIngredient(String flavour, String ingredient, double amount) {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static FlavourIngredient FlavourtoIngredient(String flavour) {
        List<FlavourIngredient> flavourIngredients = getFlavourIngredientTable();

        for (FlavourIngredient obj : flavourIngredients) {
            if (obj.flavour.toLowerCase() == flavour.toLowerCase()) {
                return obj;
            }
        }
        return null;
    }

    public static List<FlavourIngredient> getFlavourIngredientTable(){
        String sql = """
                      SELECT * FROM flavour_ingredient
                      """ ;

        List<Object[]> result = DbConnection.getDbTable(sql);
        List<FlavourIngredient> flavourIngredients = new ArrayList<>();

        for (Object[] objarray : result) {
            FlavourIngredient flavourIngredientobj = new FlavourIngredient ((String) objarray[0],(String) objarray[1] ,Double.parseDouble(objarray[2].toString()));
            flavourIngredients.add(flavourIngredientobj);
        }
        log.debug("Retrieved data from database: {}", result);
        return flavourIngredients;
    }
}
