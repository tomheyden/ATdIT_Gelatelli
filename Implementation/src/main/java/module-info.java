module atdit.gelatelli {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.kordamp.bootstrapfx.core;
    requires org.controlsfx.controls;
    requires slf4j.api;


    opens atdit.gelatelli to javafx.fxml;
    opens atdit.gelatelli.controllers to javafx.fxml;
    exports atdit.gelatelli;
    exports atdit.gelatelli.controllers to javafx.fxml;
}