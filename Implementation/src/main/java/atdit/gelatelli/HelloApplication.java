package atdit.gelatelli;

import atdit.gelatelli.views.ProductionView;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

public class HelloApplication extends Application {

    private Stage stage;
    private Scene mainScene, productionScene, warehouseScene;
    private ProgressBar loadingBar;
    private Label loadingLabel;
    private ComboBox<String> flavorComboBox;
    private Button produceButton;
    private Label recipeLabel, warehouseLabel;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        mainScene = createMainScene();
        productionScene = createProductionScene();
        warehouseScene = createWarehouseScene();
        Scene welcomeScene = createWelcomeScene();
        stage.setScene(welcomeScene);
        stage.setTitle("Eiscafè Gelatelli");
        stage.show();
    }

    private Scene createWelcomeScene() {
        BorderPane welcomeLayout = new BorderPane();
        Label welcomeLabel = new Label("Welcome to Eiscafè Gelatelli!");
        welcomeLabel.getStyleClass().add("title-label");
        Button startButton = new Button("Start Application");
        startButton.setOnAction(e -> {
            stage.setScene(mainScene);
        });
        VBox vbox = new VBox(10, welcomeLabel, startButton);
        vbox.setAlignment(Pos.CENTER);
        welcomeLayout.setCenter(vbox);
        Scene welcomeScene = new Scene(welcomeLayout, 600, 400);
        return welcomeScene;
    }

    private Scene createMainScene() {
        VBox mainLayout = new VBox();
        mainLayout.setSpacing(10);
        Label titleLabel = new Label("Eiscafè Gelatelli");
        titleLabel.getStyleClass().add("title-label");
        Button productionButton = new Button("Production");
        productionButton.setOnAction(e -> {
            // Create an instance of your new class
            ProductionView productionView = new ProductionView();
            Scene scene = new Scene(productionView, 800, 600);
            scene.getStylesheets().add("/path/to/bootstrapfx.css");
            // Set the scene of the new class to the stage
            stage.setScene(scene);
        });
        Button warehouseButton = new Button("Warehouse");
        warehouseButton.setOnAction(e -> {
            warehouseLabel.setText("Ice cream in the warehouse: 0");
            stage.setScene(warehouseScene);
        });
        mainLayout.getChildren().addAll(titleLabel, productionButton, warehouseButton);
        mainLayout.setAlignment(Pos.CENTER);
        mainScene = new Scene(mainLayout, 600, 400);
        return mainScene;
    }

    private Scene createProductionScene() {
        VBox productionLayout = new VBox();
        productionLayout.setSpacing(10);
        flavorComboBox = new ComboBox<>();
        flavorComboBox.getItems().addAll("Chocolate", "Vanilla", "Oreo", "Strawberry");
        flavorComboBox.getSelectionModel().selectFirst();
        flavorComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            recipeLabel.setText(getRecipeForFlavor(newValue));
        });
        recipeLabel = new Label();
        recipeLabel.getStyleClass().add("recipe-label");
        produceButton = new Button("Produce now!");
        produceButton.setOnAction(e -> {
            showProductionProgress();
        });
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            stage.setScene(mainScene);
        });
        productionLayout.getChildren().addAll(flavorComboBox, recipeLabel, produceButton, backButton);
        productionLayout.setAlignment(Pos.CENTER);
        productionScene = new Scene(productionLayout, 600, 400);
        return productionScene;
    }

    private Scene createWarehouseScene() {
        BorderPane warehouseLayout = new BorderPane();
        warehouseLabel = new Label("Ice cream in the warehouse: 0");
        warehouseLabel.getStyleClass().add("title-label");
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            stage.setScene(mainScene);
        });
        warehouseLayout.setCenter(warehouseLabel);
        warehouseLayout.setBottom(backButton);
        warehouseScene = new Scene(warehouseLayout, 600, 400);
        return warehouseScene;
    }

    private void showProductionProgress() {
        loadingBar = new ProgressBar();
        loadingLabel = new Label("Producing ice cream...");
        BorderPane loadingLayout = new BorderPane();
        loadingLayout.setCenter(loadingBar);

        // Add a VBox to contain the progress bar and label
        VBox progressBox = new VBox();
        progressBox.setAlignment(Pos.CENTER);
        progressBox.getChildren().addAll(loadingBar, loadingLabel);

        // Set the VBox as the center of the BorderPane
        loadingLayout.setCenter(progressBox);

        Scene loadingScene = new Scene(loadingLayout, 400, 100);
        Stage loadingStage = new Stage();
        loadingStage.setScene(loadingScene);
        loadingStage.setTitle("Producing...");
        loadingStage.show();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Random random = new Random();
                int productionTime = random.nextInt(5) + 5; // between 5 and 9 seconds
                for (int i = 0; i < productionTime; i++) {
                    Thread.sleep(1000);
                    updateProgress(i + 1, productionTime);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                loadingStage.close();
                int currentCount = Integer.parseInt(warehouseLabel.getText().split(": ")[1]);
                String flavor = flavorComboBox.getSelectionModel().getSelectedItem();
                warehouseLabel.setText("Ice cream in the warehouse: " + (currentCount + 1) + " (" + flavor + ")");
            }
        };

        loadingBar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }


    private String getRecipeForFlavor(String flavor) {
        switch (flavor) {
            case "Chocolate":
                return "Milk, sugar, cream, cocoa powder";
            case "Vanilla":
                return "Milk, sugar, cream, vanilla extract";
            case "Oreo":
                return "Milk, sugar, cream, crushed Oreos";
            case "Strawberry":
                return "Milk, sugar, cream, mashed strawberries";
            default:
                return "";
        }
    }

    public static void main(String[] args) {
        launch();
    }
}