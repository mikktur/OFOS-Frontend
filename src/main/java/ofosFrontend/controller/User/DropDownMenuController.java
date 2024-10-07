package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import ofosFrontend.AppManager;
import ofosFrontend.session.SessionManager;

import java.io.IOException;

public class DropDownMenuController extends BasicController {
    @FXML
    private AnchorPane dropDownContent;
    @FXML
    private Text dropDownUsername;


    @FXML
    private void initialize() {
        dropDownContent.getProperties().put("controller", this);
        SessionManager sessionManager = SessionManager.getInstance();
        dropDownUsername.setText(sessionManager.getUsername());

    }
    public DropDownMenuController() {
    }

    public void handleLogout() {
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.logout();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ofosFrontend/loginUI.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = AppManager.getInstance().getPrimaryStage();
            stage.setScene(new Scene(root, 600, 400));
            Stage currentStage = AppManager.getInstance().getPrimaryStage();
            currentStage.close();
            SessionManager.getInstance().logout();
            AppManager.getInstance().setPrimaryStage(stage);
            AppManager.getInstance().getPrimaryStage().setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        sessionManager.logout();
    }

    public void goToSettings(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/newUserSettingsUI.fxml"));
        Parent root = loader.load();
        mainController.resetToDefaultCartView();

        mainController.setCenterContent(root);

    }


    public void goToHistory(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/OrderHistoryUI.fxml"));
        Parent root = loader.load();
        mainController.setCenterContent(root);

    }
}
