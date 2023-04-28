package atdit.gelatelli.ressources;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class StageHelper {
    public static void showScene(Scene scene) {
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}