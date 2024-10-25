package ofosFrontend.session;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import java.io.IOException;

public class FXMLUtils {

    // Load FXML with ResourceBundle from LocalizationManager
    public static Node loadFXML(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(FXMLUtils.class.getResource(fxmlPath), LocalizationManager.getBundle());
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
