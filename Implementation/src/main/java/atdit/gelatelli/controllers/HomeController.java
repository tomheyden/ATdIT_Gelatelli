package atdit.gelatelli.controllers;

import atdit.gelatelli.Main;
import atdit.gelatelli.ressources.StageHelper;
import atdit.gelatelli.utils.ProductionService;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * The HomeController class is a controller for the Home.fxml view file.
 */
public class HomeController {

    @FXML
    private Button productionButton;

    @FXML
    private Button warehouseButton;

    @FXML
    private Label dbConnection;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    ProductionController productionController = new ProductionController();
    WarehouseController warehouseController = new WarehouseController();

    Main main = new Main();

    @FXML
    private void changeLanguageToEnglish() {
        Main.setRessource("english");
    }

    @FXML
    private void changeLanguageToGerman() {
        Main.setRessource("german");
    }

    /**
     * Initializes the controller.
     * Sets the actions for the production and warehouse buttons.
     *
     * @since 1.0
     */
     
    @FXML
    public void initialize() {

        logger.info("HomeController initialized");

        if(!Main.databaseConnection) {
            disableButtons();
            dbConnection.setVisible(true);
            dbConnection.setText("Database is not connected -- Please start the Database and restart");
            logger.warn("Database is not connected -- Buttons are disabled");
            dbConnection.setTextFill(Color.RED);
        } else {
            dbConnection.setText("Database connected");
            logger.info("Database connected succesfully -- Buttons enabled");
            dbConnection.setTextFill(Color.GREEN);
        }
        
        productionButton.setOnAction(event -> {
            logger.debug("Production button clicked");
            productionButton.getScene().getWindow().hide();
            try {
                StageHelper.showScene(main.loadProductionScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("Navigated to Production screen.");
        });

        warehouseButton.setOnAction(event -> {

            logger.debug("Warehouse button clicked");
            logger.info("Navigated to Warehouse screen");

            warehouseButton.getScene().getWindow().hide();
            try {
                StageHelper.showScene(main.loadWarehouseScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Disables the Buttons for the HomeController
     * Only called when Database is not connected
     *
     * @since 1.0
     */
    private void disableButtons() {
        productionButton.setDisable(true);
        warehouseButton.setDisable(true);
        logger.debug("Buttons on Homeview disabled");
    }
}
