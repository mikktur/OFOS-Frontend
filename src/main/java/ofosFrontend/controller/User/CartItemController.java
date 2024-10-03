package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ofosFrontend.model.CartItem;
import ofosFrontend.model.ShoppingCart;
import ofosFrontend.session.SessionManager;

import java.io.IOException;

public class CartItemController {
    @FXML
    VBox cartItemContainer;
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
    private CartItem cartItem;
    public void setCartItem(CartItem item) {
        this.cartItem = item;

        // Set initial values
        itemName.setText(item.getProduct().getProductName());
        itemPrice.setText(String.valueOf(item.getProduct().getProductPrice()));
        itemQuantity.textProperty().bind(item.quantityProperty().asString());

        // Set up event handlers
        addBtn.setOnMouseClicked(event -> item.addQuantity());
        subBtn.setOnMouseClicked(event -> {
            item.subQuantity();
            if (item.getQuantity() == 0) {
                removeCartItem();
            }
        });
        deleteBtn.setOnMouseClicked(event -> removeCartItem());
    }


    private void removeCartItem() {
        if (cartItem != null) {
            ShoppingCart cart = SessionManager.getInstance().getCart(cartItem.getRid());
            cart.removeItem(cartItem.getProduct());


            if (cartItemContainer != null) {
                cartItemContainer.getChildren().remove(cartItemContainer);
            }
        }
    }


    public Label getItemName() {
        return itemName;
    }

    public Label getItemPrice() {
        return itemPrice;
    }

    public Label getItemQuantity() {
        return itemQuantity;
    }

    public Button getAddBtn() {
        return addBtn;
    }

    public Button getSubBtn() {
        return subBtn;
    }

    public ImageView getDeleteBtn() {
        return deleteBtn;
    }
}
