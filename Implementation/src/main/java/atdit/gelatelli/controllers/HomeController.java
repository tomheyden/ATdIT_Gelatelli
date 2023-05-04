package atdit.gelatelli.controllers;

import atdit.gelatelli.ressources.StageHelper;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 * The HomeController class is a controller for the Home.fxml view file.
 */
public class HomeController {

    @FXML
    private Button productionButton;

    @FXML
    private Button warehouseButton;

    private Scene homeScene;
    private Scene productionScene;
    private Scene warehouseScene;

    /**
     * Sets the scenes used by this controller.
     *
     * @param homeScene       The scene for the home screen.
     * @param productionScene The scene for the production screen.
     * @param warehouseScene  The scene for the warehouse screen.
     */
    public void setScenes(Scene homeScene, Scene productionScene, Scene warehouseScene) {
        this.homeScene = homeScene;
        this.productionScene = productionScene;
        this.warehouseScene = warehouseScene;
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
            ProductionController.setHomeScene(homeScene);
            productionButton.getScene().getWindow().hide();
            StageHelper.showScene(productionScene);
        });

        warehouseButton.setOnAction(event -> {
            WarehouseController.setHomeScene(homeScene);
            warehouseButton.getScene().getWindow().hide();
            StageHelper.showScene(warehouseScene);
        });
    }
}
