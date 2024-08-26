module ofosfrontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens ofosFrontend to javafx.fxml;
    exports ofosFrontend;
    exports ofosFrontend.controller;
    opens ofosFrontend.controller to javafx.fxml;
}