package atdit.gelatelli.controllers;

import atdit.gelatelli.Main;
import atdit.gelatelli.ressources.StageHelper;

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
        // Set the actions for the buttons
        productionButton.setOnAction(event -> {
            productionButton.getScene().getWindow().hide();
            try {
                StageHelper.showScene(main.loadProductionScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        warehouseButton.setOnAction(event -> {
            productionButton.getScene().getWindow().hide();
            try {
                StageHelper.showScene(main.loadWarehouseScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
