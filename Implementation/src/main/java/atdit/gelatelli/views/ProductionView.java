package atdit.gelatelli.views;

import atdit.gelatelli.models.*;
import atdit.gelatelli.utils.*;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class ProductionView extends VBox {

    private final ProductionService productionService;
    private final ComboBox<String> flavourComboBox;
    private final TextField amountTextField;
    private final Button submitButton;

    public ProductionView(ProductionService productionService) {
        this.productionService = productionService;

        // create label for the flavour selection
        Label flavourLabel = new Label("Select flavour:");

        // create a list of flavours retrieved from the database
        List<Batch> ingredients = productionService.getBatchTable();

        // create a ComboBox for selecting the flavour
        flavourComboBox = new ComboBox<>();
        flavourComboBox.getItems().addAll(String.valueOf(ingredients));

        // create label for the amount input field
        Label amountLabel = new Label("Amount:");

        // create a text field for entering the amount
        amountTextField = new TextField();

        // create a button for submitting the production request
        submitButton = new Button("Produce");

        // set the action to be taken when the submit button is clicked
        submitButton.setOnAction(e -> {
            String selectedFlavour = flavourComboBox.getSelectionModel().getSelectedItem();
            int amount = Integer.parseInt(amountTextField.getText());
            productionService.produceFlavour(selectedFlavour, amount);
        });

        // add all UI components to the view
        this.getChildren().addAll(flavourLabel, flavourComboBox, amountLabel, amountTextField, submitButton);
        this.setSpacing(10);
        this.setPadding(new Insets(10));
    }
}