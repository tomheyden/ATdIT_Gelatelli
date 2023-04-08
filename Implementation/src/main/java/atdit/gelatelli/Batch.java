package atdit.gelatelli;

import java.util.Date;

public record Batch(int id, Date bbd, double amount, Ingredient ingredient) {
}
