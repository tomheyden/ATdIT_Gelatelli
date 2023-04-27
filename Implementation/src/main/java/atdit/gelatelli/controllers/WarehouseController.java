package atdit.gelatelli.controllers;

import atdit.gelatelli.models.Batch;
import atdit.gelatelli.models.Ingredient;
import atdit.gelatelli.ressources.StageHelper;
import atdit.gelatelli.utils.ProductionService;
import atdit.gelatelli.utils.WarehouseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.util.List;

public class WarehouseController {

    @FXML private Label warehouseLabel;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private Label incomingGoodsLabel;
    @FXML private ComboBox<String> goodsComboBox;
    @FXML private ChoiceBox<Integer> amountComboBox;
    @FXML private ChoiceBox<String> unitComboBox;
    @FXML private DatePicker expiryDatePicker;
    @FXML private ListView<String> warehouseListView;
    @FXML private Button deleteButton;
    @FXML private Button updateButton;
    @FXML private Button goBackButton;

    WarehouseService warehouseService = new WarehouseService();

    private static Scene staticHomeScene;

    @FXML
    private void initialize() {
        warehouseListView.getItems().addAll(warehouseService.getListContent());
        goodsComboBox.getItems().addAll(warehouseService.getIngredients("Name"));
        unitComboBox.getItems().addAll(warehouseService.getIngredients("Unit"));

        //Add numbers to Amount ChoiceBox
        ObservableList<Integer> numbers = FXCollections.observableArrayList();
        for (int i = 1; i <= 20; i++) {
            numbers.add(i);
        }
        amountComboBox.setItems(numbers);
    }

    @FXML
    private void handleDeleteButtonAction() {
        // Handle the delete button action
    }

    @FXML
    private void handleUpdateButtonAction() {
        // Handle the update button action
    }

    @FXML
    private void handleGoBackButtonAction() {
        StageHelper.showScene(staticHomeScene);
    }

    public static  void setHomeScene (Scene homeScene) {
        staticHomeScene = homeScene;
    }

}