package atdit.gelatelli.ressources;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Displays a JavaFX scene in a new stage.
 *
 * @param scene the scene to be displayed
 */
public class StageHelper {
    public static void showScene(Scene scene) {
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}