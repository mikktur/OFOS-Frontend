package ofosFrontend;

import javafx.stage.Stage;
import ofosFrontend.controller.User.MainController;

public class AppManager {

    private static AppManager instance;
    private Stage primaryStage;
    private MainController mainController;

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

    public void setMainController(MainController controller) {
        this.mainController = controller;
    }

    public MainController getMainController() {
        return mainController;
    }
}

