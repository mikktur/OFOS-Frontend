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
import java.util.List;

public class ShoppingCartController extends BasicController {
    @FXML
    VBox cartItemContainer;
    CartItemController cartItemController = new CartItemController();
    public ShoppingCartController() {

    }
    @FXML
    public void initialize() {
        try {
            loadCartItems();

        }
        catch (IOException e) {
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
    public void handleDropDownClick() {
        System.out.println("Drop down clicked");
    }
}
