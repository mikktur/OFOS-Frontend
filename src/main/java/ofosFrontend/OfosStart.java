package ofosFrontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ofosFrontend.session.GenericHelper;
import java.io.IOException;
/**
 * Main class for the application.
 */
public class OfosStart extends javafx.application.Application {
    /**
     * Starts the application GUI.
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        GenericHelper.switchLanguage("English");
        AppManager.getInstance().setPrimaryStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("loginUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("OFOS Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Main method for the application.
     */
    public static void main(String[] args) {
        //poistaa rasittavat varotukset yhessä logging.properties filun kanssa.
        System.setProperty("java.util.logging.config.file", "logging.properties");

        launch();
    }
}