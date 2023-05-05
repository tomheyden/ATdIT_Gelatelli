package atdit.gelatelli.controllers;

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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


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
    private Label productionStatusLabel;
    @FXML
    private ListView<String> receiptListView;
    @FXML
    private ListView<String> warehouseListView;
    @FXML
    private ListView<String> activityListView;
    @FXML
    private Button goBackButton;
    private static final Logger logger = LoggerFactory.getLogger(ProductionController.class);
    WarehouseService warehouseService = new WarehouseService();

    List<Batch> warehouseList;

    private static Scene staticHomeScene;

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
     * and updates the progress bar accordingly. If there are not, it displays an error message.
     */
    @FXML
    private void handleProduceButtonAction() {
        logger.debug("Produce Button clicked");

        System.out.println("Produce Button");
        String selectedFlavour = flavorComboBox.getValue();
        Double selectedAmount = Double.valueOf(sizeChoiceBox.getValue());

        System.out.println(selectedAmount);
        System.out.println(selectedFlavour);

        logger.debug("Selected Flavor: {}, Selected Amount: {}", selectedFlavour, selectedAmount);

        if (ProductionService.checkIfEnoughIngredients(selectedFlavour, selectedAmount)) {
            ProductionService.produceFlavour(selectedFlavour, selectedAmount);
            progessBarDisplay("green");
            logger.info("Successfully produced {} of {} flavor", selectedAmount, selectedFlavour);
        } else {
            progessBarDisplay("red");
            logger.warn("Could not produce {} of {} flavor due to insufficient ingredients", selectedAmount, selectedFlavour);
        }
    }

    /**
     * This method is called when the Production View is loaded. It populates the warehouseListView with the content
     * returned by the WarehouseService and the flavorComboBox with the available flavors returned by the
     * ProductionService. It also initializes the sizeChoiceBox with the numbers from 1 to 20.
     */
    public void initialize() {
        logger.info("Initializing ProductionController");
        productionStatusLabel.setVisible(false);
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
            StageHelper.showScene(staticHomeScene);
        });
    }

    /**
     * Sets the static home scene for this controller. This is used to switch back to the home scene when the "Go back" button is pressed.
     *
     * @param homeScene the static home scene
     */
    public static void setHomeScene(Scene homeScene) {
        logger.debug("Setting home scene");
        staticHomeScene = homeScene;
    }

    private void progessBarDisplay(String color) {
        logger.debug("Displaying progress bar");
        productionProgressBar.setStyle("-fx-accent: blue;");
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(productionProgressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(productionProgressBar.progressProperty(), 1)));
        timeline.play();

        timeline.setOnFinished(e -> {
            productionStatusLabel.setVisible(true);
            if (color.equalsIgnoreCase(color)) {
                logger.warn("Error in production: {}", ProductionService.errorOfIngredientsamount);
                productionProgressBar.setStyle("-fx-accent: " + color + " ;");
                productionStatusLabel.setText(ProductionService.errorOfIngredientsamount);
            } else {
                logger.info("Production finished successfully");
                productionProgressBar.setStyle("-fx-accent: green ;");
                productionStatusLabel.setText("Finished");
            }
        });
    }
}
