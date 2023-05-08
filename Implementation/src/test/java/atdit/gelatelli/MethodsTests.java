package atdit.gelatelli;

import atdit.gelatelli.models.Batch;
import atdit.gelatelli.utils.DbConnection;
import atdit.gelatelli.utils.ProductionService;
import atdit.gelatelli.utils.WarehouseService;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MethodsTests {

    List<Batch> datalist = new ArrayList<>();
    List<Batch> batchList = new ArrayList<>();

    java.sql.Date date = new java.sql.Date(2023-1900, Calendar.AUGUST, 28);
    LocalDate localDate = date.toLocalDate().plusDays(1);

    WarehouseService warehouseService = new WarehouseService();

    @Order(1)
    @Test
    public void testInsert () {

        System.out.println("");
        ProductionService.getListContent("Chocolate");

        //Insert Ingredients for Chocolate production
        datalist.add(new Batch(DbConnection.getMaxId("batch"),java.sql.Date.valueOf(localDate),0.50,"Bittersweet Chocolate"));
        datalist.add(new Batch(DbConnection.getMaxId("batch"),java.sql.Date.valueOf(localDate),15.0,"Egg Yolk"));
        datalist.add(new Batch(DbConnection.getMaxId("batch"),java.sql.Date.valueOf(localDate),0.50,"Fine Sea Salt"));
        datalist.add(new Batch(DbConnection.getMaxId("batch"),java.sql.Date.valueOf(localDate),0.75,"Granulated Sugar"));
        datalist.add(new Batch(DbConnection.getMaxId("batch"),java.sql.Date.valueOf(localDate),2.50,"Heavy Cream"));
        datalist.add(new Batch(DbConnection.getMaxId("batch"),java.sql.Date.valueOf(localDate),0.25,"Unsweetened Cocoa Powder"));
        datalist.add(new Batch(DbConnection.getMaxId("batch"),java.sql.Date.valueOf(localDate),2.50,"Vanilla Extract"));
        datalist.add(new Batch(DbConnection.getMaxId("batch"),java.sql.Date.valueOf(localDate),1.25,"Whole Milk"));

        for (Batch batch : datalist) {
            warehouseService.insertIngredient(batch);
        }

        Assertions.assertEquals(true,ProductionService.checkIfEnoughIngredients("Chocolate",1.0));

    }

    @Order(2)
    @Test
    public void testReading() {

        batchList = ProductionService.getBatchTable();
        Assertions.assertEquals(true, batchList.containsAll(datalist));
        batchList.size();
    }

    @Order(3)
    @Test
    public void testProduce() {

        ProductionService.produceFlavour("Chocolate",1.0);
        List<Batch> batchListAfterInsert = ProductionService.getBatchTable();

        Assertions.assertEquals(false,ProductionService.checkIfEnoughIngredients("Chocolate",1.0));
        System.out.println(batchList);
        Assertions.assertEquals(batchList.size()-8, batchListAfterInsert.size());

    }
}
