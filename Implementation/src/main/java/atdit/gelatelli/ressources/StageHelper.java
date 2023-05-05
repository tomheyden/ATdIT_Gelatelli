package atdit.gelatelli.ressources;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a JavaFX scene in a new stage.
 *
 * @param scene the scene to be displayed
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
}