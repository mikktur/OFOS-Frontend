package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ofosFrontend.session.CartManager;

import java.io.IOException;

/**
 * This class is used to control the main UI of the application.
 * It is responsible for:
 * <ul>
 *   <li>Loading the default content into the center pane.</li>
 *   <li>Setting and updating the center content of the main UI.</li>
 *   <li>Setting up child controllers, including NavBar, ShoppingCart, and others.</li>
 *   <li>Managing the visibility of the side menu and shopping cart.</li>
 * </ul>
 * The MainController acts as the central hub of the application, coordinating interactions
 * between various UI components and controllers. in short it is used to the "what" to show part of the application.
 */
public class UserMainController {

    @FXML
    private StackPane centerPane;

    @FXML
    private BorderPane root;
    @FXML
    private VBox cart;
    @FXML
    ShoppingCartController shoppingCartController;
    @FXML
    NavController navController;
    @FXML
    private StackPane navBar;
    @FXML
    private AnchorPane dropDownRoot;
    @FXML
    DropDownMenuController dropDownMenuController;
    MMenuController mmController;
    private CartManager cartManager = new CartManager();

    @FXML
    public void initialize() {
        setControllers();
        System.out.println("Main controller initialized");

        root.setLeft(null);
        root.setRight(null);
        loadDefaultContent();
    }


    public void setCenterContent(Node content) {
        if (centerPane == null) {
            System.out.println("centerPane is null!");
            return;
        }

        centerPane.getChildren().clear();
        centerPane.getChildren().add(content);
        StackPane.setAlignment(content, Pos.CENTER);
    }


    public void loadDefaultContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/mainUI.fxml"));

            Node content = loader.load();
            mmController = loader.getController();

            if (mmController != null) {
                mmController.setMainController(this);
            }
            resetToDefaultCartView();
            setCenterContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setControllers() {
        try {
            shoppingCartController = (ShoppingCartController) cart.getProperties().get("controller");
            if (shoppingCartController != null) {
                shoppingCartController.setMainController(this);
            }

            navController = (NavController) navBar.getProperties().get("controller");
            if (navController != null) {
                navController.setMainController(this);
            }

            dropDownMenuController = (DropDownMenuController) dropDownRoot.getProperties().get("controller");
            if (dropDownMenuController != null) {
                dropDownMenuController.setMainController(this);
            }
        } catch (Exception e) {
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
            shoppingCartController.updateCart();
            boolean isVisible = cart.isVisible();
            cart.setVisible(!isVisible);
            root.setRight(isVisible ? null : cart);
        }

    }

    public void filterRestaurants(String query) {
        if (mmController != null) {
            mmController.filterRestaurants(query);
        }
    }


    public ShoppingCartController getShoppingCartController() {
        return shoppingCartController;
    }

    public void resetToDefaultCartView() {
        if(shoppingCartController == null) {
            return;
        }
        shoppingCartController.resetCartView();
    }
}

