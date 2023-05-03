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
import javafx.util.Duration;

import java.util.List;

public class ProductionController {

    @FXML private AnchorPane productionAnchorPane;
    @FXML private ComboBox<String> flavorComboBox;
    @FXML private ChoiceBox<Integer> sizeChoiceBox;
    @FXML private Button showRecipeButton;
    @FXML private Button produceButton;
    @FXML private ProgressBar productionProgressBar;
    @FXML private Label productionStatusLabel;
    @FXML private ListView<String> receiptListView;
    @FXML private ListView<String> warehouseListView;
    @FXML private ListView<String> activityListView;
    @FXML private Button goBackButton;

    WarehouseService warehouseService = new WarehouseService();

    List<Batch> warehouseList;

    private static Scene staticHomeScene;

    @FXML
    private void handleShowRecipeButtonAction() {
        receiptListView.getItems().clear();
        receiptListView.getItems().addAll(ProductionService.getListContent(flavorComboBox.getValue()));
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
            //System.out.println("Not enough Ingredients -- Please get at least " + productionAmount +" of "+ProductionService.FlavourtoIngredient(selectedFlavour));
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(productionProgressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(productionProgressBar.progressProperty(), 1)));
        timeline.play();

        timeline.setOnFinished(e -> {
            productionStatusLabel.setText("Finished");
        });
    }

    public void initialize() {

        warehouseListView.getItems().addAll(warehouseService.getListContent());
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
