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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;

public class AdminController {
    @FXML
    public HBox adminNavBar;
    private static final Logger logger = LogManager.getLogger();
    @FXML
    private StackPane ownerCenterPane;
    @FXML
    private BorderPane ownerRoot;
    private AdminViewFactory adminViewFactory;
    public AdminController() {
        // required by FXML loader
    }

    @FXML
    public void initialize() {
        adminViewFactory = new AdminViewFactory(this);
        loadDefaultContent();
        setupControllers();

    }
    public void setCenterContent(Node content) {
        if (ownerCenterPane == null) {
            logger.log(Level.ERROR,"centerPane is null!");
            return;
        }

        ownerCenterPane.getChildren().clear();
        ownerCenterPane.getChildren().add(content);
        StackPane.setAlignment(content, Pos.CENTER);
    }

    public void loadDefaultContent() {
            Parent content = adminViewFactory.createAdminHomeView();
            setCenterContent(content);

    }

    public void loadRestaurantContent(Restaurant restaurant) {
        Parent content = adminViewFactory.createAdminRestaurantView(restaurant);
        setCenterContent(content);
    }
    public void reloadPage(){
        adminViewFactory.reloadPage();
    }
    public void setupControllers() {
        try {
            AdminNavController navController = (AdminNavController) adminNavBar.getProperties().get("controller");
            if (navController != null) {
                navController.setMainController(this);
            } else {
                logger.log(Level.INFO, "navController is null!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
