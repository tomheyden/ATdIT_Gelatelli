package atdit.gelatelli.controllers;

import atdit.gelatelli.Main;
import atdit.gelatelli.models.Batch;
import atdit.gelatelli.ressources.StageHelper;
import atdit.gelatelli.utils.WarehouseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * The WarehouseController class controls the UI for the batch management system.
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
    private Button updateButton;
    @FXML
    private Button warehouseGoBackButton;
    @FXML
    private Button insertButton;
    
    private static final Logger logger = LoggerFactory.getLogger(ProductionController.class);
    
    // WarehouseService instance for interacting with the database
    WarehouseService warehouseService = new WarehouseService();
    Button getGoBackButton = new Button();
    
    // Flag indicating whether an item has been inserted
    private boolean inserted;

    Main main = new Main();
    StageHelper stageHelper = new StageHelper();

    /**
     * Initializes the UI components and sets their initial values.
     */
    @FXML
    private void initialize() {
        logger.debug("Initializing the UI components and setting their initial values.");
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
        logger.debug("Handling the delete button action.");
        // Handle the delete button action
    }

    /**
     * Handles the "Insert" button action. Inserts a new inventory item into the database.
     */
    @FXML
    private void handleInsertButton() {
        logger.debug("Handling the insert button action.");
        doneLabel.setText("");
        doneLabel.setVisible(true);
        boolean fieldsempty = false;

        // Validate that all required fields have been filled out
        if (expiryDatePicker.getValue() == null) {
            stageHelper.handleEmptyBox(expiryDatePicker, doneLabel, "Date");
            fieldsempty = true;
            logger.warn("Expiry date is not selected");
        } else {
            expiryDatePicker.setStyle("-fx-border-color: WHITE;");
        }

        if (amountComboBox.getValue() == null) {
            stageHelper.handleEmptyBox(amountComboBox, doneLabel, "Amount");
            fieldsempty = true;
            logger.warn("Amount is not selected");
        } else {
            amountComboBox.setStyle("-fx-border-color: WHITE;");
        }

        if (goodsComboBox.getValue() == null) {
            stageHelper.handleEmptyBox(goodsComboBox, doneLabel, "Ingredient");
            fieldsempty = true;
            logger.warn("Ingredient is not selected");
        } else {
            goodsComboBox.setStyle("-fx-border-color: WHITE;");
        }

        // If all fields have been filled out, insert the new item into the database
        if (!fieldsempty) {
            warehouseService.insertIngredient(new Batch(0, Date.valueOf(expiryDatePicker.getValue()), amountComboBox.getValue(), goodsComboBox.getValue()));
            doneLabel.setText("Done");
            doneLabel.setTextFill(Color.GREEN);
            doneLabel.setVisible(true);
            inserted = true;

            refreshItems();

            inserted = true;
            logger.info("New inventory item added to the database: {} amount of {} with expiry date {}");
        }
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
            logger.info("batch list has been refreshed");
        } else {
            hintLabel.setText("No Items inserted after last refresh");
            hintLabel.setTextFill(Color.RED);
            logger.warn("No items have been inserted since the last refresh");
        }
    }

    /**
     * Handles the action when the go back button is clicked.
     * <p>
     * Shows the static home scene and refreshes the input fields.
     */
    @FXML
    private void handleGoBackButtonAction() {

        logger.debug("Go Back button clicked");
        
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
        logger.debug("Refreshing input fields");

        try {
            expiryDatePicker.setValue(null);
            expiryDatePicker.setStyle("-fx-border-color: WHITE;");
            amountComboBox.getItems().clear();
            amountComboBox.setStyle("-fx-border-color: WHITE;");
            goodsComboBox.getItems().clear();
            goodsComboBox.setStyle("-fx-border-color: WHITE;");

            doneLabel.setVisible(false);
            hintLabel.setVisible(false);

        } catch(NullPointerException e) {e.printStackTrace();logger.error("Null pointer exception when refreshing item.", e);}
    }
}