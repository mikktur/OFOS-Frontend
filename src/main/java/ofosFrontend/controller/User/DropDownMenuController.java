package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import ofosFrontend.session.SessionManager;

public class DropDownMenuController {
    @FXML
    private Text dropDownUsername;

    @FXML
    private void initialize() {
        SessionManager sessionManager = SessionManager.getInstance();
        dropDownUsername.setText(sessionManager.getUsername());
    }

    public DropDownMenuController() {
    }

    public void handleLogout() {
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.logout();
    }


}
