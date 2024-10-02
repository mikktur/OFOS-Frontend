package ofosFrontend.controller.User;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ofosFrontend.AppManager;

public abstract class BasicController {
    public BasicController() {
    }


    public void goToMain() {
        try {




            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/mainUI.fxml"));
            Parent newCenterContent = loader.load();

            BorderPane rootPane = (BorderPane) AppManager.getInstance().getPrimaryStage().getScene().getRoot();


            rootPane.setCenter(newCenterContent);
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