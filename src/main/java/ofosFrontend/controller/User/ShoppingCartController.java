package ofosFrontend.controller.User;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    private Text subTotalLabel;
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
        ObservableList<CartItem> items = SessionManager.getInstance().getCart().getItems();


        items.addListener((ListChangeListener<CartItem>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    removeEmptyCartMessage();
                    for (CartItem addedItem : change.getAddedSubList()) {
                        try {
                            addCartItemToUI(addedItem);
                            addQuantityListener(addedItem);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (change.wasRemoved()) {
                    for (CartItem removedItem : change.getRemoved()) {
                        removeCartItemFromUI(removedItem);
                    }
                    if (items.isEmpty()) {
                        displayEmptyCartMessage();
                    }
                }
            }

            updateSubTotal();
        });

        cartItemContainer.getChildren().clear();
        if (items.isEmpty()) {
            displayEmptyCartMessage();
        } else {
            for (CartItem item : items) {
                addCartItemToUI(item);
                addQuantityListener(item);
            }
        }


        updateSubTotal();
    }

    private void addCartItemToUI(CartItem item) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/cartItem.fxml"));
        VBox cartItem = loader.load();
        CartItemController cartItemController = loader.getController();
        cartItemController.loadCartItem(item);

        cartItem.setUserData(item);
        cartItemContainer.getChildren().add(cartItem);
    }

    private void removeCartItemFromUI(CartItem item) {
        cartItemContainer.getChildren().removeIf(node -> {
            CartItem associatedItem = (CartItem) node.getUserData();
            return associatedItem != null && associatedItem.getProduct().getProductID().equals(item.getProduct().getProductID());
        });
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
    private void removeEmptyCartMessage() {
        cartItemContainer.getChildren().removeIf(node -> "emptyMessage".equals(node.getId()));
    }

    public void displayEmptyCartMessage() {
        cartItemContainer.getChildren().clear();
        Label emptyItem = new Label("Your cart is empty");
        emptyItem.setId("emptyMessage");
        cartItemContainer.getChildren().add(emptyItem);
    }


    private void addQuantityListener(CartItem cartItem) {
        cartItem.quantityProperty().addListener((observable, oldValue, newValue) -> {
            updateSubTotal();
        });
    }

    private void updateSubTotal() {
        double subTotal = SessionManager.getInstance().getCart().getTotalPrice();
        subTotalLabel.setText("Subtotal: " + subTotal);
    }
}
