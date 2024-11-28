package ofosFrontend;

import javafx.stage.Stage;
import ofosFrontend.controller.User.UserMainController;

/**
 * Singleton class for managing the application.
 */
public class AppManager {

    private static AppManager instance;
    private Stage primaryStage;

    private AppManager() {
    }

    /**
     * Gets the instance of the AppManager
     * @return The AppManager
     */
    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * Sets the primary stage for the application
     * @param stage The primary stage
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Gets the primary stage for the application
     * @return The primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }




}

