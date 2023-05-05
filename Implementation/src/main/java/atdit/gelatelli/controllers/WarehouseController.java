package atdit.gelatelli.controllers;

import atdit.gelatelli.Main;
import atdit.gelatelli.models.Batch;
import atdit.gelatelli.models.Ingredient;
import atdit.gelatelli.ressources.StageHelper;
import atdit.gelatelli.utils.ProductionService;
import atdit.gelatelli.utils.WarehouseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.LabelSkin;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The WarehouseController class controls the UI for the warehouse management system.
 * It includes methods for handling user actions on the UI components, such as inserting
 * and deleting inventory items, and refreshing the view.
 *
 * @version 1.4
 * @since 03/05/2023
 */
public class WarehouseController {

    // FXML UI components
    @FXML
    private Label warehouseLabel;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private Label incomingGoodsLabel;
    @FXML
    private Label doneLabel;
    @FXML
    private Label hintLabel;
    @FXML
    private ComboBox<String> goodsComboBox;
    @FXML
    private ChoiceBox<Integer> amountComboBox;
    @FXML
    private ChoiceBox<String> unitComboBox;
    @FXML
    private DatePicker expiryDatePicker;
    @FXML
    private ListView<String> warehouseListView;
    @FXML
    private Button deleteButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button warehouseGoBackButton;
    @FXML
    private Button insertButton;

    // WarehouseService instance for interacting with the database
    WarehouseService warehouseService = new WarehouseService();
    Button getGoBackButton = new Button();
    // Flag indicating whether an item has been inserted
    private boolean inserted;

    Main main = new Main();

    /**
     * Initializes the UI components and sets their initial values.
     */
    @FXML
    private void initialize() {
        doneLabel.setVisible(false);
        inserted = false;
        warehouseListView.getItems().addAll(warehouseService.getListContent());
        goodsComboBox.getItems().addAll(warehouseService.getIngredients("Name"));
        unitComboBox.getItems().addAll(warehouseService.getIngredients("Unit"));

        // Add numbers to Amount ChoiceBox
        ObservableList<Integer> numbers = FXCollections.observableArrayList();
        for (int i = 1; i <= 20; i++) {
            numbers.add(i);
        }
        amountComboBox.setItems(numbers);

        expiryDatePicker.setStyle("-fx-control-inner-background: white;");
        amountComboBox.setStyle("-fx-control-inner-background: white;");
        goodsComboBox.setStyle("-fx-control-inner-background: white;");
    }

    /**
     * Handles the "Delete" button action.
     */
    @FXML
    private void handleDeleteButtonAction() {
        // Handle the delete button action
    }

    /**
     * Handles the "Insert" button action. Inserts a new inventory item into the database.
     */
    @FXML
    private void handleInsertButton() {

        doneLabel.setText("");
        doneLabel.setVisible(true);
        boolean fieldsempty = true;

        // Validate that all required fields have been filled out
        if (expiryDatePicker.getValue() == null) {
            handleEmptyBox(expiryDatePicker, doneLabel, "Date");
            fieldsempty = false;
        } else {
            expiryDatePicker.setStyle("-fx-border-color: WHITE;");
        }

        if (amountComboBox.getValue() == null) {
            handleEmptyBox(amountComboBox, doneLabel, "Amount");
            fieldsempty = false;
        } else {
            amountComboBox.setStyle("-fx-border-color: WHITE;");
        }

        if (goodsComboBox.getValue() == null) {
            handleEmptyBox(goodsComboBox, doneLabel, "Ingredient");
            fieldsempty = false;
        } else {
            goodsComboBox.setStyle("-fx-border-color: WHITE;");
        }

        // If all fields have been filled out, insert the new item into the database
        if (fieldsempty) {
            WarehouseService.insertIngredient(new Batch(0, Date.valueOf(expiryDatePicker.getValue()), amountComboBox.getValue(), goodsComboBox.getValue()));
            doneLabel.setText("Done");
            doneLabel.setTextFill(Color.GREEN);
            doneLabel.setVisible(true);
            inserted = true;

            refreshItems();

            inserted = true;
        }
    }

    /**
     * Handles the case where a required input field is empty.
     *
     * @param control The control representing the empty input field.
     * @param label   The label to display the error message.
     * @param type    The type of the input field.
     */
    private void handleEmptyBox(Control control, Label label, String type) {
        control.setStyle("-fx-border-color: RED;");
        label.setText("Values are missing");
        label.setTextFill(Color.RED);
    }

    /**
     * Handles the action when the update button is clicked.
     * <p>
     * If items have been inserted, clears the warehouseListView, adds all the items in the
     * warehouseService's list to the warehouseListView, and sets inserted to false. Otherwise,
     * sets hintLabel to display a message indicating that no items have been inserted since the
     * last refresh.
     */
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

    /**
     * Handles the action when the go back button is clicked.
     * <p>
     * Shows the static home scene and refreshes the input fields.
     */
    @FXML
    private void handleGoBackButtonAction() {

        warehouseGoBackButton.getScene().getWindow().hide();
        try {
            StageHelper.showScene(main.loadHomeScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshItems();
    }

    /**
     * Refreshes all the input fields.
     * <p>
     * Clears the text fields and choice boxes, sets date picker and spinner values to null, and
     * clears the items in table and list views.
     */
    private void refreshItems() {
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
            } catch (NullPointerException e) {
                System.out.println("Item is empty");
                continue;
            }
        }
    }

    public void setResourceBundle(String bundleName, Scene scene) {
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName);

    }
}