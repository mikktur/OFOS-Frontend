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
    VBox cartItemContainer;
    @FXML
    CartItemController cartItemController = new CartItemController();
    @FXML
    private Button checkoutBtn;

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
            VBox cartItem = cartItemController.loadCartItem(item);
            cartItemContainer.getChildren().add(cartItem);
        }
    }

    public void handleCheckoutClick() {
        System.out.println("Checkout clicked");
    }

    public void addListeners() {
        System.out.println("Listener added");
        checkoutBtn.setOnAction(event -> {
            handleCheckoutClick();
        });

    }
}
