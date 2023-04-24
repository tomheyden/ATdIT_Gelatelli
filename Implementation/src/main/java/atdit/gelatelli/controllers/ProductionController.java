package atdit.gelatelli.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ProductionController {

    @FXML
    private Accordion accordion;

    @FXML
    private ComboBox<String> flavorComboBox;

    @FXML
    private TextArea recipeTextArea;

    @FXML
    private Button showRecipeButton;

    @FXML
    private ChoiceBox<String> sizeChoiceBox;

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

    @FXML
    private void handleShowRecipeButtonAction() {
        // Code to show the recipe for the selected flavor
    }

    @FXML
    private void handleProduceButtonAction() {
        // Code to start the production process for the selected size and flavor
    }

}
