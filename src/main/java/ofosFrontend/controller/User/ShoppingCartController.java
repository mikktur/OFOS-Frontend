package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ofosFrontend.model.CartItem;
import ofosFrontend.model.Product;
import ofosFrontend.session.SessionManager;

import java.io.IOException;
import java.util.EventListener;
import java.util.List;

public class ShoppingCartController {
    @FXML
    private VBox cartItemContainer;

    @FXML
    private Button goToCheckout;

    public ShoppingCartController() {

    }

    @FXML
    public void initialize() {
        try {
            loadCartItems();
            addListeners();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCartItems() throws IOException {
        List<CartItem> list = SessionManager.getInstance().getCart().getItems();
        for (CartItem item : list) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/cartItem.fxml"));
            VBox cartItem = loader.load();  // Load the FXML and controller
            CartItemController cartItemController = loader.getController();  // Get the controller for each item
            cartItemController.loadCartItem(item);  // Pass the CartItem to the controller
            cartItemContainer.getChildren().add(cartItem);  // Add the item to the container
        }
    }

    public void handleCheckoutClick() {
        System.out.println("Checkout clicked");
    }

    public void addListeners() {
        System.out.println("Listener added");

        goToCheckout.setOnAction(event -> {
            handleCheckoutClick();
        });

    }
}
