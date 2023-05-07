package atdit.gelatelli.ressources;

import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a JavaFX scene in a new stage.
 *
 */
public class StageHelper {
    private static final Logger logger = LoggerFactory.getLogger(StageHelper.class);
    public static void showScene(Scene scene) {
        logger.info("Displaying JavaFX scene in a new stage");
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        logger.trace("JavaFX scene displayed in the new stage");
    }

    /**
     * Handles the case where a required input field is empty.
     *
     * @param control The control representing the empty input field.
     * @param label   The label to display the error message.
     * @param type    The type of the input field.
     */
    public void handleEmptyBox(Control control, Label label, String type) {
        control.setStyle("-fx-border-color: RED;");
        label.setText("Values are missing");
        label.setTextFill(Color.RED);
        logger.warn("Required input field of type {} is empty", type);
    }
}