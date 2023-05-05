package atdit.gelatelli.controllers;

import atdit.gelatelli.Main;
import atdit.gelatelli.ressources.StageHelper;
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
        // Set the actions for the buttons
        productionButton.setOnAction(event -> {
            logger.debug("Production button clicked");

            ProductionController.setHomeScene(homeScene);
            productionButton.getScene().getWindow().hide();
            StageHelper.showScene(productionScene);

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
}
