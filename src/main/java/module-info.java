module ofosFrontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires java.net.http;
    requires java.prefs;
    requires org.apache.logging.log4j.core;
    requires com.fasterxml.jackson.databind;
    requires java.logging;

    opens ofosFrontend.model to com.fasterxml.jackson.databind; // Open package to Jackson
    opens ofosFrontend.controller to javafx.fxml; // Existing configuration
    exports ofosFrontend;
    exports ofosFrontend.controller;
    exports ofosFrontend.controller.Owner;
    exports ofosFrontend.controller.User;
    exports ofosFrontend.model;
    exports ofosFrontend.service;
    opens ofosFrontend.controller.User to javafx.fxml;
    opens ofosFrontend.controller.Owner to javafx.fxml;
    exports ofosFrontend.controller.User.userSettings;
    opens ofosFrontend.controller.User.userSettings to javafx.fxml;
}