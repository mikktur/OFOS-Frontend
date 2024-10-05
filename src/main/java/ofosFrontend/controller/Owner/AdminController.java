package ofosFrontend.controller.Owner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import ofosFrontend.controller.AdminMenuController;

import java.io.IOException;

public class AdminController {
    @FXML
    public HBox adminNavBar;
    @FXML
    private StackPane ownerCenterPane;
    @FXML
    private BorderPane ownerRoot;

    public AdminController() {
    }

    @FXML
    public void initialize() {
        loadDefaultContent();
        setupControllers();

    }
    public void setCenterContent(Node content) {
        if (ownerCenterPane == null) {
            System.out.println("centerPane is null!");
            return;
        }

        ownerCenterPane.getChildren().clear();
        ownerCenterPane.getChildren().add(content);
        StackPane.setAlignment(content, Pos.CENTER);
    }

    public void loadDefaultContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/Owner/adminMainUI.fxml"));
            Node content = loader.load();
            AdminMainMenuController adminMenuController = loader.getController();
            if (adminMenuController != null) {
                adminMenuController.setMainController(this);
            }

            setCenterContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    public void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/loginUI.fxml"));
            Node root = loader.load();
            ownerRoot.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
