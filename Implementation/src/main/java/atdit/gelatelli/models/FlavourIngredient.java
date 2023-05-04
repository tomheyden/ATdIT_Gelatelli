package atdit.gelatelli.models;

/**
 * A record that represents an ingredient used in a flavor of ice cream and its quantity.
 */
public record FlavourIngredient(String flavour, String ingredient, double amount) {
}
