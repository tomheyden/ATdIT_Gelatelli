package atdit.gelatelli.models;

import atdit.gelatelli.utils.DbConnection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Batch implements Comparable<Batch>{

    private int id;
    private Date bbd;
    private double amount;
    private Ingredient ingredient;

    public Batch (int id, Date bbd, double amount, String ingredient) {
        this.id = id;
        this.bbd = bbd;
        this.amount = amount;
        this.ingredient = new Ingredient(ingredient,(Double)null,null);
    }

    @Override
    public int compareTo(Batch o) {
        return this.bbd.compareTo(o.bbd);
    }

    public static List<Batch> getBatchTable () {
        List<Batch> batchlist = new ArrayList<>();

        String sql = "SELECT * FROM warehouse ORDER BY ddb ASC";
        List<Map<String, Object>> rows = DbConnection.getDbTable(sql);
        for (Map<String, Object> row : rows) {
            Batch batch = new Batch(
                    (int) row.get("id"),
                    (Date) row.get("bbd"),
                    (double) row.get("amount"),
                    (String) row.get("ingredient_name")
            );
            batchlist.add(batch);
        }
        return batchlist;
    }

}
