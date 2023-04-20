module atdit.gelatelli {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;


    opens atdit.gelatelli to javafx.fxml;
    exports atdit.gelatelli;
}