package atdit.gelatelli;

import atdit.gelatelli.controllers.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The main class for the Ice Cream Shop application.
 */
public class Main extends Application {

    private Stage stage;

    private Scene homeScene;
    private HomeController homeController;

    private Scene productionScene;
    private ProductionController productionController;

    private Scene warehouseScene;
    private WarehouseController warehouseController;

    @FXML
    private VBox homeAnchorPane;

    /**
     * Starts the application.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        // Load the FXML files and create controllers
        FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("home.fxml"));
        homeScene = new Scene(homeLoader.load());
        homeController = homeLoader.getController();

        FXMLLoader productionLoader = new FXMLLoader(getClass().getResource("Production.fxml"));
        productionScene = new Scene(productionLoader.load());
        productionController = productionLoader.getController();

        FXMLLoader warehouseLoader = new FXMLLoader(getClass().getResource("warehouse.fxml"));
        warehouseScene = new Scene(warehouseLoader.load());
        warehouseController = warehouseLoader.getController();

        homeController.setScenes(homeScene,productionScene,warehouseScene);

        // Set the main scene to the Home view
        stage.setScene(homeScene);
        stage.setTitle("Ice Cream Shop");
        stage.show();
    }

    /**
     * Launches the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}




