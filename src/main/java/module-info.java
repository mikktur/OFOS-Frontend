module ofosfrontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;

    opens ofosFrontend.model to com.fasterxml.jackson.databind; // Open package to Jackson
    opens ofosFrontend.controller to javafx.fxml; // Existing configuration
    exports ofosFrontend;
    exports ofosFrontend.controller;
}