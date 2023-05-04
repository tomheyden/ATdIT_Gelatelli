package atdit.gelatelli.models;

import atdit.gelatelli.utils.WarehouseService;

import java.util.Date;

/**
 * Represents a batch of a particular ingredient.
 */
public record Batch(
        /**
         * The unique identifier for the batch.
         */
        int id,
        /**
         * The "best before" date for the batch.
         */
        Date bbd,
        /**
         * The amount of the ingredient in the batch.
         */
        double amount,
        /**
         * The name of the ingredient in the batch.
         */
        String ingredient) {

}

