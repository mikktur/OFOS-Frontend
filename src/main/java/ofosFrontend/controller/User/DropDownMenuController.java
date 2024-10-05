package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

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

        Stage currentStage = (Stage) AppManager.getInstance().getPrimaryStage();

        Scene historyScene = new Scene(root, 800, 600);

        currentStage.setTitle("OFOS Order History");
        currentStage.setMinWidth(800);
        currentStage.setMinHeight(600);

        currentStage.setScene(historyScene);

        currentStage.show();
    }
}
