package atdit.gelatelli.controllers;

import atdit.gelatelli.models.Batch;
import atdit.gelatelli.ressources.StageHelper;
import atdit.gelatelli.utils.ProductionService;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class ProductionController {

    @FXML
    private ComboBox<String> flavorComboBox;

    @FXML
    private TextArea recipeTextArea;

    @FXML
    private Button showRecipeButton;

    @FXML
    private ChoiceBox<Integer> sizeChoiceBox;

    @FXML
    private Button produceButton;

    @FXML
    private ProgressBar productionProgressBar;

    @FXML
    private Label productionStatusLabel;

    @FXML
    private AnchorPane root;

    @FXML
    private HBox flavorHBox;

    @FXML
    private Text flavorText;

    List<Batch> warehouseList;

    @FXML
    private Button goBackButton;

    private static Scene staticHomeScene;

    @FXML
    private void handleShowRecipeButtonAction() {
        recipeTextArea.clear();
        recipeTextArea.appendText(ProductionService.getRecipeforFlavour(flavorComboBox.getValue()));
        System.out.println("Show Recipe Button");
    }

    @FXML
    private void handleProduceButtonAction() {
        System.out.println("Produce Button");
        String selectedFlavour = flavorComboBox.getValue();
        Double selectedAmount = Double.valueOf(sizeChoiceBox.getValue());

        double productionAmount = ProductionService.getProductionAmount(selectedFlavour,selectedAmount);

        System.out.println(selectedAmount);
        System.out.println(selectedFlavour);

        if (ProductionService.checkifenoughIngredients(selectedFlavour,productionAmount)) {
            ProductionService.produceFlavour(selectedFlavour, selectedAmount);
        } else {
            System.out.println("Not enough Ingredients -- Please get at least " + productionAmount +" of "+ProductionService.FlavourtoIngredient(selectedFlavour));
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(productionProgressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(productionProgressBar.progressProperty(), 1)));
        timeline.play();

        timeline.setOnFinished(e -> {
            productionStatusLabel.setText("Finished");
        });
    }

    public void initialize() {
        flavorComboBox.getItems().addAll(ProductionService.getFlavourTable());

        //Add numbers to Amount ChoiceBox
        ObservableList<Integer> numbers = FXCollections.observableArrayList();
        for (int i = 1; i <= 20; i++) {
            numbers.add(i);
        }
        sizeChoiceBox.setItems(numbers);
        //sizeChoiceBox.getItems().add()

        goBackButton.setOnAction(event -> {
            goBackButton.getScene().getWindow().hide();
            StageHelper.showScene(staticHomeScene);
        });
    }

    public static  void setHomeScene (Scene homeScene) {
        staticHomeScene = homeScene;
    }

}
