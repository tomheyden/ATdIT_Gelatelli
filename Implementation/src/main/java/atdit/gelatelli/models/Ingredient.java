package atdit.gelatelli.models;
import atdit.gelatelli.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ingredient implements Comparable<Ingredient> {
        private static final Logger log = LoggerFactory.getLogger( MethodHandles.lookup().lookupClass() );

        private String name;
        private double purchasePrice;
        private String unit;

        public String getUnit() {
            return this.unit;
        }

    public String getName() {
        return this.name;
    }

        public Ingredient (String name, double purchasePrice, String unit) {
            this.name = name;
            this.purchasePrice = purchasePrice;
            this.unit = unit;
        }

        @Override
        public int compareTo(Ingredient o) {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return this.name.equals(((Ingredient) o).name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        public static List<Ingredient> readIngredients() {
                log.info("Starting readfromDBtoWE method...");
                String sql1 = "SELECT * from ingredient" ;

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
                        Ingredient ingredient_temp = new Ingredient((String)temp[0],Double.parseDouble(temp[1].toString()),(String) temp[2]);
                        ingredients.add(ingredient_temp);

                        log.trace("Adding Ingredient object to list: {}", ingredient_temp);
                    } catch (Exception e) {
                        log.error("Error converting data to Ingredient objects: {}", e.getMessage());
                    }
                }
                return ingredients;
            }

    }
