package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ofosFrontend.session.FXMLUtils;
import ofosFrontend.session.CartManager;
import ofosFrontend.session.LocalizationManager;

import java.io.IOException;

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
            // Initialize the FXMLLoader and load the FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/mainUI.fxml"));
            loader.setResources(LocalizationManager.getBundle());
            Node mainContent = loader.load();

            mmController = loader.getController();
            if (mmController != null) {
                mmController.setMainController(this);
            } else {
                System.out.println("mmController is null");
            }

            resetToDefaultCartView();
            setCenterContent(mainContent);
            reloadDropDown();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reloadDropDown() {
        try {
            FXMLLoader dropDownLoader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/dropDownUI.fxml"));
            dropDownLoader.setResources(LocalizationManager.getBundle());
            dropDownRoot = dropDownLoader.load();
            dropDownMenuController = dropDownLoader.getController();
            dropDownMenuController.setMainController(this);

            root.setLeft(dropDownRoot);
            toggleSideMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setControllers() {
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

    public void hideRedDot() {
        if (navController != null) {
            navController.hideRedDot();
        }
    }

    public void showRedDot() {
        if (navController != null) {
            navController.showRedDot();
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
        if (shoppingCartController == null) {
            return;
        }
        shoppingCartController.resetCartView();
    }
}


