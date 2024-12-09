package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import ofosFrontend.AppManager;
import ofosFrontend.session.SessionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Controller for the drop down menu in the user interface
 * Contains methods to navigate to the admin dashboard, settings and history views
 * Also contains a method to handle logout
 */
public class DropDownMenuController extends BasicController {
    @FXML
    private AnchorPane dropDownContent;
    @FXML
    private Text dropDownUsername;
    @FXML
    private HBox adminDashboardElement;
    private final Logger logger = LogManager.getLogger(DropDownMenuController.class);
    /**
     * Initializes the drop down menu
     * Sets the username in the drop down menu to the current user's username
     * If the user is an admin, makes the admin dashboard element visible
     */
    @FXML
    private void initialize() {
        dropDownContent.getProperties().put("controller", this);
        SessionManager sessionManager = SessionManager.getInstance();
        dropDownUsername.setText(sessionManager.getUsername());
        String role = sessionManager.getRole();

        if ("ADMIN".equals(role)) {
            // Make the admin dashboard element visible
            adminDashboardElement.setVisible(true);
            adminDashboardElement.setManaged(true);
        }
    }

    /**
     * Navigates to the admin dashboard view
     */
    @FXML
    public void goToAdminDashboard(MouseEvent mouseEvent) {
        mainController.loadAdminDashboardView();
    }



    public DropDownMenuController() {
        // required by FXML loader
    }

    /**
     * Handles the logout of the user
     * Logs the user out and navigates to the login view
     */
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
            logger.error("Failed to load login view: {}", e.getMessage());
        }
        sessionManager.logout();
    }

    /**
     * Navigates to the settings view
     * @param mouseEvent the mouse event that triggered the method
     * @throws IOException if the view cannot be loaded
     */
    public void goToSettings(MouseEvent mouseEvent) throws IOException {
        mainController.loadSettingsView();
    }

    /**
     * Navigates to the history view
     * @param mouseEvent the mouse event that triggered the method
     * @throws IOException if the view cannot be loaded
     */
    public void goToHistory(MouseEvent mouseEvent) throws IOException {
        mainController.loadHistoryView();

    }


}
