package atdit.gelatelli.controllers;

import atdit.gelatelli.Main;
import atdit.gelatelli.models.Batch;
import atdit.gelatelli.ressources.StageHelper;
import atdit.gelatelli.utils.ProductionService;
import atdit.gelatelli.utils.WarehouseService;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;


/**
 * This class is the controller for the Production View, which allows the user to produce flavors and check the
 * production's progress. It provides methods to handle user's input and display data on the view.
 *
 * @version 1.1
 * @since 27/04/2023
 */
public class ProductionController {

    @FXML
    private AnchorPane productionAnchorPane;
    @FXML
    private ComboBox<String> flavorComboBox;
    @FXML
    private ChoiceBox<Integer> sizeChoiceBox;
    @FXML
    private Button showRecipeButton;
    @FXML
    private Button produceButton;
    @FXML
    private ProgressBar productionProgressBar;
    @FXML
    private TextArea productionStatusLabel;
    @FXML
    private ListView<String> receiptListView;
    @FXML
    private ListView<String> warehouseListView;
    @FXML
    private ListView<String> activityListView;
    @FXML
    private Button goBackButton;
    @FXML
    private Label doneLabelProduction;

    private static final Logger logger = LogManager.getLogger();

    WarehouseService warehouseService = new WarehouseService();
    Main main = new Main();
    StageHelper stageHelper = new StageHelper();

    /**
     * This method is called when the Production View is loaded. It populates the warehouseListView with the content
     * returned by the WarehouseService and the flavorComboBox with the available flavors returned by the
     * ProductionService.
     * <p>
     * It also initializes the sizeChoiceBox with the numbers from 1 to 20.
     */
    public void initialize() {
        logger.info("Initializing ProductionController");
        productionStatusLabel.setVisible(false);
        doneLabelProduction.setVisible(false);
        warehouseListView.getItems().addAll(warehouseService.getListContent());
        flavorComboBox.getItems().addAll(ProductionService.getFlavourTable());

        //Add numbers to Amount ChoiceBox
        ObservableList<Integer> numbers = FXCollections.observableArrayList();
        for (int i = 1; i <= 20; i++) {
            numbers.add(i);
        }
        sizeChoiceBox.setItems(numbers);

        goBackButton.setOnAction(event -> {

            logger.debug("Go back button pressed");
            goBackButton.getScene().getWindow().hide();
            try {
                StageHelper.showScene(main.loadHomeScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This method is called when the "Show Recipe" button is clicked by the user. It clears the receiptListView and
     * populates it with the recipe for the selected flavor.
     */
    @FXML
    private void handleShowRecipeButtonAction() {
        logger.debug("Show Recipe Button clicked");

        receiptListView.getItems().clear();
        receiptListView.getItems().addAll(ProductionService.getListContent(flavorComboBox.getValue()));
        System.out.println("Show Recipe Button");
    }

    /**
     * This method is called when the "Produce" button is clicked by the user. It checks if there are enough
     * ingredients in the warehouse to produce the selected flavor and amount. If there are, it produces the flavor
     * and updates the progress batchbar accordingly. If there are not, it displays an error message.
     */
    @FXML
    private void handleProduceButtonAction() {
        logger.debug("Produce Button clicked");

        if (!respondToControlContent()) {
            String selectedFlavour = flavorComboBox.getValue();
            Double selectedAmount = Double.valueOf(sizeChoiceBox.getValue());
            doneLabelProduction.setVisible(false);
            logger.debug("Selected Flavor: {}, Selected Amount: {}", selectedFlavour, selectedAmount);

            if (ProductionService.checkIfEnoughIngredients(selectedFlavour, selectedAmount)) {
                ProductionService.produceFlavour(selectedFlavour, selectedAmount);
                productionStatusLabel.setText("Finished");

                progessBarDisplay(true);
                logger.info("Successfully produced {} of {} flavor", selectedAmount, selectedFlavour);
            } else {
                progessBarDisplay(false);
                logger.warn("Could not produce {} of {} flavor due to insufficient ingredients", selectedAmount, selectedFlavour);
            }
        }
    }

    /**
     * This method is being called when the Production Button is clicked.
     * <p>
     * On the basis of its parameter, there is either a successful response with the Label
     * being set to "Finished" and a green bar or an unsuccessful response with the Label
     * displaying the missing ingredients and a red bar
     *
     * @param successful    information about whether or not the Production was successful
     *
     */
    private void progessBarDisplay(boolean successful) {
    
        logger.debug("Displaying progress bar");
        productionProgressBar.setStyle("-fx-accent: blue;");
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(productionProgressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(productionProgressBar.progressProperty(), 1)));
        timeline.play();

        timeline.setOnFinished(e -> {
            productionStatusLabel.setVisible(true);
            if (!successful) {
                logger.warn("Error in production: {}", ProductionService.errorOfIngredientsamount);
                productionProgressBar.setStyle("-fx-accent: red ;");
                productionStatusLabel.setText(ProductionService.errorOfIngredientsamount);
            } else {
                logger.info("Production finished successfully");
                String finished = "Finished";
                productionStatusLabel.setText(finished);
                productionProgressBar.setStyle("-fx-accent: green ;");
            }
        });
    }

    /**
     * This method is called to respond to whether or not a Control returns null and responds to it.
     * Control(s) with no selected items are highlighted with a red border
     * Control(s) with selected items get a white border
     *
     * @return true if there is at minimum one Control that returns null (contains no data)
     */
    private boolean respondToControlContent() {
        // Validate that all required fields have been filled out
        if (flavorComboBox.getValue() == null) {
            stageHelper.handleEmptyBox(flavorComboBox, doneLabelProduction, "Date");
            logger.warn("Flavour is not selected");
            return true;
        } else {
            flavorComboBox.setStyle("-fx-border-color: WHITE;");
        }

        if (sizeChoiceBox.getValue() == null) {
            stageHelper.handleEmptyBox(sizeChoiceBox, doneLabelProduction, "Amount");
            logger.warn("Amount is not selected");
            return true;
        } else {
            sizeChoiceBox.setStyle("-fx-border-color: WHITE;");
        }
        return false;
    }
}
