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
import javafx.scene.control.skin.LabelSkin;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;

import java.sql.Date;

import java.util.ArrayList;
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

        expiryDatePicker.setStyle("-fx-control-inner-background: white;");
        amountComboBox.setStyle("-fx-control-inner-background: white;");
        goodsComboBox.setStyle("-fx-control-inner-background: white;");
    }

    @FXML
    private void handleDeleteButtonAction() {
        // Handle the delete button action
    }

    @FXML
    private void handleInsertButton() {

        doneLabel.setText("");
        doneLabel.setVisible(true);
        boolean fieldsempty = true;

        if (expiryDatePicker.getValue() == null) {
            handleEmptyBox(expiryDatePicker,doneLabel,"Date");
            fieldsempty = false;
        } else {
            expiryDatePicker.setStyle("-fx-border-color: WHITE;");
        }

        if (amountComboBox.getValue() == null) {
            handleEmptyBox(amountComboBox,doneLabel,"Amount");
            fieldsempty = false;
        } else {
            amountComboBox.setStyle("-fx-border-color: WHITE;");
        }

        if (goodsComboBox.getValue() == null) {
            handleEmptyBox(goodsComboBox,doneLabel,"Ingredient");
            fieldsempty = false;
        } else {
            goodsComboBox.setStyle("-fx-border-color: WHITE;");
        }

        if (fieldsempty) {
            WarehouseService.insertIngredient(new Batch(0,Date.valueOf(expiryDatePicker.getValue()),amountComboBox.getValue(),goodsComboBox.getValue()));
            doneLabel.setText("Done");
            doneLabel.setTextFill(Color.GREEN);
            doneLabel.setVisible(true);
            inserted = true;

            refreshItems();

            inserted = true;
        }
    }


    private void handleEmptyBox (Control control, Label label, String type) {
        control.setStyle("-fx-border-color: RED;");
        label.setText("Values are missing");
        label.setTextFill(Color.RED);
    }

    @FXML
    private void handleUpdateButtonAction() {
        if (inserted) {
            warehouseListView.getItems().clear();
            warehouseListView.getItems().addAll(warehouseService.getListContent());
            hintLabel.setText("Refreshed");
            hintLabel.setTextFill(Color.GREEN);
            inserted = false;
        } else {
            hintLabel.setText("No Items inserted after last refresh");
            hintLabel.setTextFill(Color.RED);
        }
    }

    @FXML
    private void handleGoBackButtonAction() {
        StageHelper.showScene(staticHomeScene);
        refreshItems();
    }

    public static  void setHomeScene (Scene homeScene) {
        staticHomeScene = homeScene;
    }

    private void refreshItems () {
        List<Control> controls = new ArrayList<>();
        controls.add(expiryDatePicker);
        controls.add(amountComboBox);
        controls.add(goodsComboBox);
        controls.add(filterComboBox);

        List<Label> labels = new ArrayList<>();
        labels.add(doneLabel);
        labels.add(hintLabel);

        for (Label label : labels) {
            label.setVisible(false);
        }

        for (Control control : controls) {
            try {
                control.setStyle("-fx-border-color: WHITE;");

                if (control instanceof TextField) {
                    ((TextField) control).clear();
                } else if (control instanceof TextArea) {
                    ((TextArea) control).clear();
                } else if (control instanceof ChoiceBox) {
                    ((ChoiceBox<?>) control).setValue(null);
                } else if (control instanceof ComboBox) {
                    ((ComboBox<?>) control).setValue(null);
                } else if (control instanceof DatePicker) {
                    ((DatePicker) control).setValue(null);
                } else if (control instanceof Spinner) {
                    ((Spinner<?>) control).getValueFactory().setValue(null);
                } else if (control instanceof TableView) {
                    ((TableView<?>) control).getItems().clear();
                } else if (control instanceof ListView) {
                    ((ListView<?>) control).getItems().clear();
                }
            } catch (NullPointerException e) {System.out.println("Item is empty"); continue;}
        }
    }
}