package ofosFrontend;

import javafx.stage.Stage;
import ofosFrontend.controller.User.UserMainController;

public class AppManager {

    private static AppManager instance;
    private Stage primaryStage;
    private UserMainController userMainController;

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

    public void setMainController(UserMainController controller) {
        this.userMainController = controller;
    }

    public UserMainController getMainController() {
        return userMainController;
    }
}

