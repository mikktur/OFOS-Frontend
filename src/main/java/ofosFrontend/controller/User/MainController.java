package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane centerPane;

    @FXML
    private BorderPane root;
    @FXML
    private VBox cart;

    @FXML
    private StackPane navBar;
    @FXML
    private AnchorPane dropDownRoot;
    private boolean isSideMenuVisible = false;
    private boolean isShoppingCartVisible = false;

    @FXML
    public void initialize() {

        loadDefaultContent();
        setControllers();
        root.setLeft(null);
        root.setRight(null);
    }


    public void setCenterContent(Node content) {
        if (centerPane == null) {
            System.out.println("centerPane is null!");
            return;
        }
        System.out.println("Setting center content...");
        System.out.println("Content: " + content);
        // Clear any existing content
        centerPane.getChildren().clear();
        System.out.println("Cleared existing content...");
        System.out.println("Content: " + centerPane.getChildren());
        // Add the new content
        centerPane.getChildren().add(content);
        System.out.println("content: " + centerPane.getChildren());
        // Ensure the content is set to fill the available space (optional)
        StackPane.setAlignment(content, Pos.CENTER);
    }


    public void loadDefaultContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/mainUI.fxml"));
            Node content = loader.load();
            setCenterContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setControllers() {
        try {
            // Set up NavBarController
            NavController navController = (NavController) navBar.getProperties().get("controller");
            if (navController != null) {
                navController.setMainController(this);
            } else {
                System.out.println("NavController is null!");
            }

            // Set up ShoppingCartController
            ShoppingCartController shoppingCartController = (ShoppingCartController) cart.getProperties().get("controller");
            if (shoppingCartController != null) {
                shoppingCartController.setMainController(this);
            } else {
                System.out.println("ShoppingCartController is null!");
            }
            // Set up DropDownMenuController
            DropDownMenuController dropDownMenuController = (DropDownMenuController) dropDownRoot.getProperties().get("controller");
            if (dropDownMenuController != null) {
                dropDownMenuController.setMainController(this);
            } else {
                System.out.println("DropDownMenuController is null!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void switchToContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node content = loader.load();
            setCenterContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void toggleSideMenu() {
        if (dropDownRoot != null) {
            boolean isVisible = dropDownRoot.isVisible();
            dropDownRoot.setVisible(!isVisible);
            root.setLeft(isVisible ? null : dropDownRoot);
        }
    }

    public void toggleShoppingCart() {
        if (cart != null) {
            boolean isVisible = cart.isVisible();
            cart.setVisible(!isVisible);
            root.setRight(isVisible ? null : cart);
        }
    }

}

