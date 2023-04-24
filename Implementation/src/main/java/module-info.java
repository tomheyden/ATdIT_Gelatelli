module atdit.gelatelli {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;
    requires org.kordamp.bootstrapfx.core;
    requires org.controlsfx.controls;

    opens atdit.gelatelli to javafx.fxml;
    opens atdit.gelatelli.controllers to javafx.fxml;
    exports atdit.gelatelli;
    exports atdit.gelatelli.controllers to javafx.fxml;
}