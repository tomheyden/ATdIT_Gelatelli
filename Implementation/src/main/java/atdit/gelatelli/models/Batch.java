package atdit.gelatelli.models;

import atdit.gelatelli.utils.WarehouseService;

import java.util.Date;

public record Batch ( int id, Date bbd, double amount, String ingredient){

}

