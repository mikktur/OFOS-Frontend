package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ofosFrontend.model.ShoppingCart;
import ofosFrontend.session.SessionManager;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class MainController {

    @FXML
    private StackPane centerPane;

    @FXML
    private BorderPane root;
    @FXML
    private VBox cart;
    @FXML
    ShoppingCartController shoppingCartController;
    @FXML
    private StackPane navBar;
    @FXML
    private AnchorPane dropDownRoot;


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
        centerPane.getChildren().clear();
        centerPane.getChildren().add(content);
        StackPane.setAlignment(content, Pos.CENTER);
    }


    public void loadDefaultContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/mainUI.fxml"));

            Node content = loader.load();
            MMenuController mmController = loader.getController();
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
            // Set up NavBarController
            NavController navController = (NavController) navBar.getProperties().get("controller");
            if (navController != null) {
                navController.setMainController(this);
            } else {
                System.out.println("NavController is null!");
            }

            // Set up ShoppingCartControllerrrrrrr
            shoppingCartController = (ShoppingCartController) cart.getProperties().get("controller");
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

            // Toggle the cart's visibility
            boolean isVisible = cart.isVisible();
            cart.setVisible(!isVisible);
            root.setRight(isVisible ? null : cart);


        }

    }

    public ShoppingCartController getShoppingCartController() {
        return shoppingCartController;
    }
    public void checkEmptyCarts() {
        SessionManager sessionManager = SessionManager.getInstance();
        Iterator<Map.Entry<Integer, ShoppingCart>> iterator = sessionManager.getCartMap().entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, ShoppingCart> entry = iterator.next();
            ShoppingCart cart = entry.getValue();
            if (cart.getItems().isEmpty()) {
                iterator.remove();
            }
        }
    }
    public void resetToDefaultCartView() {
        if(shoppingCartController == null) {
            return;
        }
        shoppingCartController.setRid(0);
        checkEmptyCarts();
        shoppingCartController.updateCart();
    }
}

