package ofosFrontend.controller.User;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ofosFrontend.AppManager;

public abstract class BasicController {
    public BasicController() {
    }


    public void goToMain() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/mainUI.fxml"));
        try {
            Parent root = loader.load();
            Stage currentStage = AppManager.getInstance().getPrimaryStage();
            Scene menuScene = new Scene(root, 1000, 800);
            currentStage.setTitle("OFOS Menu");
            currentStage.setScene(menuScene);
            currentStage.show();
            MMenuController controller = loader.getController();
            controller.initMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/loginUI.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) AppManager.getInstance().getPrimaryStage();


            Scene loginScene = new Scene(root, 650, 400);

            currentStage.setTitle("OFOS Login");

            currentStage.setScene(loginScene);

            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}