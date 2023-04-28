package atdit.gelatelli.controllers;

import atdit.gelatelli.models.Batch;
import atdit.gelatelli.models.Ingredient;
import atdit.gelatelli.ressources.StageHelper;
import atdit.gelatelli.utils.ProductionService;
import atdit.gelatelli.utils.WarehouseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.sql.Date;

import java.util.List;

public class WarehouseController {

    @FXML private Label warehouseLabel;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private Label incomingGoodsLabel;
    @FXML private Label doneLabel;
    @FXML private Label hintLabel;
    @FXML private ComboBox<String> goodsComboBox;
    @FXML private ChoiceBox<Integer> amountComboBox;
    @FXML private ChoiceBox<String> unitComboBox;
    @FXML private DatePicker expiryDatePicker;
    @FXML private ListView<String> warehouseListView;
    @FXML private Button deleteButton;
    @FXML private Button updateButton;
    @FXML private Button goBackButton;
    @FXML private Button insertButton;

    WarehouseService warehouseService = new WarehouseService();

    private boolean inserted;

    private static Scene staticHomeScene;

    @FXML
    private void initialize() {
        doneLabel.setVisible(false);
        inserted = false;
        warehouseListView.getItems().addAll(warehouseService.getListContent());
        goodsComboBox.getItems().addAll(warehouseService.getIngredients("Name"));
        unitComboBox.getItems().addAll(warehouseService.getIngredients("Unit"));

        //Add numbers to Amount ChoiceBox
        ObservableList<Integer> numbers = FXCollections.observableArrayList();
        for (int i = 1; i <= 20; i++) {
            numbers.add(i);
        }
        amountComboBox.setItems(numbers);
    }

    @FXML
    private void handleDeleteButtonAction() {
        // Handle the delete button action
    }

    @FXML
    private void handleInsertButton() {
        WarehouseService.insertIngredient(new Batch(0,Date.valueOf(expiryDatePicker.getValue()),amountComboBox.getValue(),goodsComboBox.getValue()));
        doneLabel.setVisible(true);
        inserted = true;
    }

    @FXML
    private void handleUpdateButtonAction() {
        if (inserted) {
            warehouseListView.getItems().addAll(warehouseService.getListContent());
            hintLabel.setText("Refreshed");
            hintLabel.setTextFill(Color.GREEN);
        } else {
            hintLabel.setText("No Items inserted after last refresh");
            hintLabel.setTextFill(Color.RED);
        }
    }

    @FXML
    private void handleGoBackButtonAction() {
        StageHelper.showScene(staticHomeScene);
    }

    public static  void setHomeScene (Scene homeScene) {
        staticHomeScene = homeScene;
    }

}