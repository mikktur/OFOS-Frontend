package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane centerPane; // This StackPane is used for dynamic content
 // This StackPane is used for the navigation bar

    @FXML
    private BorderPane root; // This BorderPane is the root layout of the application
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
        root.setLeft(null); // Initialize without the side menu
        root.setRight(null); // Initialize without the shopping cart
    }

    // Method to set the center content dynamically
    public void setCenterContent(Node content) {
        centerPane.getChildren().clear(); // Clear any existing content
        centerPane.getChildren().add(content); // Add new content to the centerPane
    }

    // Example method to load default content in the centerPane
    private void loadDefaultContent() {
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

    // This method can be called by other controllers or UI components to change the center content
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

    // Toggle the shopping cart visibility
    public void toggleShoppingCart() {
        if (cart != null) {
            boolean isVisible = cart.isVisible();
            cart.setVisible(!isVisible);
            root.setRight(isVisible ? null : cart);
        }
    }

}

