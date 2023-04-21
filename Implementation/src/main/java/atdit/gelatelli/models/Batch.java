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
}
