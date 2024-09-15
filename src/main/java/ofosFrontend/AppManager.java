package ofosFrontend;

import javafx.stage.Stage;

public class AppManager {

    private static AppManager instance;
    private Stage primaryStage;

    private AppManager() {
    }

    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}

