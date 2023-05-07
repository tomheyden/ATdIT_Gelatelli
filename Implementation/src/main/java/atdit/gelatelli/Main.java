package atdit.gelatelli;

import atdit.gelatelli.controllers.*;
import atdit.gelatelli.utils.DbConnection;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * The main class for the Ice Cream Shop application.
 */
public class Main extends Application {

    private Stage stage;

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("english");

    public static boolean databaseConnection = true;

    @FXML
    private VBox homeAnchorPane;

    /**
     * Starts the application.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if an error occurs during application startup
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("Starting Gelatelli IT System...");
        stage = primaryStage;
        
        try {
            DbConnection.getDbConnection();
        } catch (SQLException e) {databaseConnection = false;}


        // Set the main scene to the Home view
        stage.setScene(loadHomeScene());
        stage.setTitle("IT System Gelatelli");
        stage.show();
    }

    public static void setRessource(String properties) {
        resourceBundle = ResourceBundle.getBundle(properties);
    }

    public Scene loadHomeScene() throws IOException {
        FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("home.fxml"),resourceBundle);
        Scene homeScene = new Scene(homeLoader.load());
        HomeController homeController = homeLoader.getController();
        return homeScene;
    }

    public Scene loadProductionScene() throws IOException {
        FXMLLoader productionLoader = new FXMLLoader(getClass().getResource("Production.fxml"),resourceBundle);
        Scene productionScene = new Scene(productionLoader.load());
        ProductionController productionController = productionLoader.getController();
        return productionScene;
    }
    
    public Scene loadWarehouseScene() throws IOException {
        FXMLLoader warehouseLoader = new FXMLLoader(getClass().getResource("warehouse.fxml"),resourceBundle);
        Scene warehouseScene = new Scene(warehouseLoader.load());
        WarehouseController warehouseController = warehouseLoader.getController();
        return warehouseScene;
    }

    /**
     * Launches the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        logger.info("Launching Gelatelli IT System");
        launch(args);
    }

}




