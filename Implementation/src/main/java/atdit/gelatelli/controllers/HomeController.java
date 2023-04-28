package atdit.gelatelli.controllers;
import atdit.gelatelli.ressources.*;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class HomeController {

    @FXML
    private Button productionButton;

    @FXML
    private Button warehouseButton;

    private Scene homeScene;
    private Scene productionScene;
    private Scene warehouseScene;


    public void setScenes(Scene homeScene, Scene productionScene, Scene warehouseScene) {
        this.homeScene = homeScene;
        this.productionScene = productionScene;
        this.warehouseScene = warehouseScene;
    }

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