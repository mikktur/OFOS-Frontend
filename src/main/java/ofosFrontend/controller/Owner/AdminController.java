package ofosFrontend.controller.Owner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ofosFrontend.AppManager;
import ofosFrontend.model.Restaurant;
import ofosFrontend.session.SessionManager;

import java.io.IOException;

/**
 * Controller for the owner view
 */
public class AdminController {
    @FXML
    public HBox adminNavBar;
    @FXML
    private StackPane ownerCenterPane;
    @FXML
    private BorderPane ownerRoot;
    private AdminViewFactory adminViewFactory;
    public AdminController() {
    }

    /**
     * Initialize the owner controller
     * Load the default content
     */
    @FXML
    public void initialize() {
        adminViewFactory = new AdminViewFactory(this);
        loadDefaultContent();
        setupControllers();

    }

    /**
     * Set the center content of the owner view
     * @param content the content to set
     */
    public void setCenterContent(Node content) {
        if (ownerCenterPane == null) {
            System.out.println("centerPane is null!");
            return;
        }

        ownerCenterPane.getChildren().clear();
        ownerCenterPane.getChildren().add(content);
        StackPane.setAlignment(content, Pos.CENTER);
    }

    /**
     * Load the default content
     */
    public void loadDefaultContent() {
            Parent content = adminViewFactory.createAdminHomeView();
            setCenterContent(content);
    }

    /**
     * Load the restaurant content
     * @param restaurant The restaurant to display
     */
    public void loadRestaurantContent(Restaurant restaurant) {
        Parent content = adminViewFactory.createAdminRestaurantView(restaurant);
        setCenterContent(content);
    }

    /**
     * Reload the page
     */
    public void reloadPage(){
        adminViewFactory.reloadPage();
    }

    /**
     * Set up the controllers
     */
    public void setupControllers() {
        try {
            AdminNavController navController = (AdminNavController) adminNavBar.getProperties().get("controller");
            if (navController != null) {
                navController.setMainController(this);
            } else {
                System.out.println("navController is null!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Log out the user
     */
    public void logout() {
        SessionManager adminSessionManager = SessionManager.getInstance();
        adminSessionManager.logout();
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
        adminSessionManager.logout();
    }
}
