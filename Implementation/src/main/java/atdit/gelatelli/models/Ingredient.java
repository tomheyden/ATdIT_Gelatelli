package atdit.gelatelli.models;

import atdit.gelatelli.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Represents an ingredient used in the production of icecream.
 */
public class Ingredient implements Comparable<Ingredient> {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private String name;
    private double purchasePrice;
    private String unit;

    /**
     * Returns the unit of measurement for the ingredient.
     *
     * @return the unit of measurement for the ingredient
     */
    public String getUnit() {
        logger.debug("Getting unit for ingredient: {}", this.name);
        return this.unit;
    }

    /**
     * Returns the name of the ingredient.
     *
     * @return the name of the ingredient
     */
    public String getName() {
        logger.debug("Getting name for ingredient: {}", this.name);
        return this.name;
    }

    /**
     * Constructs an Ingredient object.
     *
     * @param name          the name of the ingredient
     * @param purchasePrice the purchase price of the ingredient
     * @param unit          the unit of measurement for the ingredient
     */
    public Ingredient(String name, double purchasePrice, String unit) {
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.unit = unit;

        logger.info("Created new ingredient: name={}, purchasePrice={}, unit={}", name, purchasePrice, unit);
    }

    /**
     * Compares this ingredient to another ingredient based on their names.
     *
     * @param o the other ingredient to compare to
     * @return 0 if the names are equal, a negative integer if this ingredient's name comes before the other ingredient's
     * name lexicographically, or a positive integer if this ingredient's name comes after the other ingredient's name
     * lexicographically
     */
    @Override
    public int compareTo(Ingredient o) {
        logger.debug("Comparing ingredient: {} with ingredient: {}", this.name, o.getName());
        return this.name.compareToIgnoreCase(o.getName());
    }

    /**
     * Determines if this ingredient is equal to another object.
     *
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        boolean isEqual = this.name.equalsIgnoreCase(that.name);
        if (!isEqual) {
            logger.debug("Ingredients are not equal. This ingredient: {}, Other ingredient: {}", this.name, that.getName());
        }
        return isEqual;
    }

    /**
     * Returns a hash code value for this ingredient.
     *
     * @return a hash code value for this ingredient
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Reads all ingredients from the database and returns them in a list.
     *
     * @return a list of all ingredients in the database
     */
    public static List<Ingredient> readIngredients() {
        logger.info("Starting readfromDBtoWE method...");
        String sql1 = "SELECT * from ingredient";

        List<Object[]> result = DbConnection.getDbTable(sql1);
        List<Ingredient> ingredients = new ArrayList<>();
        int i;

        for (Object[] objarray : result) {
            i = 0;
            Object[] temp = new Object[result.toArray().length - 1];
            for (Object obj : objarray) {
                temp[i] = obj;
                i++;
            }

            try {
                Ingredient ingredient_temp = new Ingredient((String) temp[0], Double.parseDouble(temp[1].toString()), (String) temp[2]);
                ingredients.add(ingredient_temp);

                logger.trace("Adding Ingredient object to list: {}", ingredient_temp);
            } catch (Exception e) {
                logger.error("Error converting data to Ingredient objects: {}", e.getMessage());
            }
        }
        return ingredients;
    }

}
