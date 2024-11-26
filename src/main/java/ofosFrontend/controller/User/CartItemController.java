package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ofosFrontend.model.CartItem;
import ofosFrontend.model.ShoppingCart;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;

import java.text.NumberFormat;

/**
 * Controller for the cart item in the shopping cart, used to make ui cards for the cart items
 * @see CartItem for the model
 */
public class CartItemController {
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(LocalizationManager.getLocale());
    @FXML
    private Button addBtn;
    @FXML
    private Button subBtn;
    @FXML
    private ImageView deleteBtn;
    @FXML
    private Label itemName;
    @FXML
    private Label itemPrice;
    @FXML
    private Label itemQuantity;
    private VBox cartItemNode;
    private CartItem cartItem;

    /**
     * Sets the cart item and the node that represents it
     * @param item the cart item
     * @param cartItemNode the node that represents the cart item
     */
    public void setCartItem(CartItem item, VBox cartItemNode) {
        this.cartItem = item;
        this.cartItemNode = cartItemNode;


        itemName.setText(item.getProduct().getProductName());
        itemPrice.setText(currencyFormatter.format(item.getProduct().getProductPrice()));
        itemQuantity.textProperty().bind(item.quantityProperty().asString());


        addBtn.setOnMouseClicked(event -> item.addQuantity());
        subBtn.setOnMouseClicked(event -> {
            item.subQuantity();
            if (item.getQuantity() == 0) {
                removeCartItem();
            }
        });
        deleteBtn.setOnMouseClicked(event -> removeCartItem());
    }

    /**
     * Removes the cart item from the shopping cart
     */
    private void removeCartItem() {
        if (cartItem != null) {

            ShoppingCart cart = SessionManager.getInstance().getCart(cartItem.getRid());
            System.out.println("Removing item: " + cartItem.getProduct().getProductName());
            cart.removeItem(cartItem.getProduct());


            if (cartItemNode != null && cartItemNode.getParent() instanceof VBox parentContainer) {
                parentContainer.getChildren().remove(cartItemNode);
            }
        }
    }


}
