module ofosfrontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires java.desktop;

    opens ofosFrontend.model to com.fasterxml.jackson.databind; // Open package to Jackson
    opens ofosFrontend.controller to javafx.fxml; // Existing configuration
    exports ofosFrontend;
    exports ofosFrontend.controller;
    exports ofosFrontend.controller.User;
    opens ofosFrontend.controller.User to javafx.fxml;
}