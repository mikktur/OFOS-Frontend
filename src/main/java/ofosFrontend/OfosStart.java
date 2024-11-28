package ofosFrontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ofosFrontend.controller.LoginController;
import ofosFrontend.session.GenericHelper;
import ofosFrontend.session.LocalizationManager;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

public class OfosStart extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        GenericHelper.switchLanguage("English");
        AppManager.getInstance().setPrimaryStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(OfosStart.class.getResource("loginUI.fxml"));
        fxmlLoader.setResources(LocalizationManager.getBundle());
        System.out.println(LocalizationManager.getBundle().getLocale());
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("OFOS Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        launch();
    }
}