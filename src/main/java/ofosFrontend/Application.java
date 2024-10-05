package ofosFrontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        AppManager.getInstance().setPrimaryStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("loginUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("OFOS Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //poistaa rasittavat varotukset yhessä logging.properties filun kanssa.
        System.setProperty("java.util.logging.config.file", "logging.properties");

        launch();
    }
}