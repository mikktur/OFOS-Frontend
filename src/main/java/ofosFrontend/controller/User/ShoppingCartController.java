package ofosFrontend.controller.User;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ofosFrontend.AppManager;
import ofosFrontend.model.CartItem;
import ofosFrontend.model.Product;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.ShoppingCart;
import ofosFrontend.session.SessionManager;

import java.io.IOException;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCartController {

    @FXML
    private VBox cartItemContainer;
    @FXML
    private Text subTotalLabel;
    @FXML
    private Button goToCheckout;
    @FXML
    private VBox cartRoot;
    private MainController mainController;
    private int rid = 0;
    public ShoppingCartController() {
    }

    @FXML
    public void initialize() {

        cartRoot.getProperties().put("controller", this);
        System.out.println("ShoppingCartController initialized");
        System.out.println("RID: " + rid);
        addListeners();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void loadCartItems() throws IOException {
        ObservableList<CartItem> items = SessionManager.getInstance().getCart(rid).getItems();
        System.out.println("Entered loadCartItems AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

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
    public void loadAllUserCartItems() {
        // Get the cartMap from SessionManager
        HashMap<Integer, ShoppingCart> userCarts = SessionManager.getInstance().getCartMap();

        cartItemContainer.getChildren().clear(); // Clear previous items

        for (Map.Entry<Integer, ShoppingCart> entry : userCarts.entrySet()) {
            ShoppingCart cart = entry.getValue();
            try {
                // Load the FXML for each cart card
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/shoppingCarts.fxml"));
                Node cartCardNode = loader.load();

                // Use lookup to find the label and button in the card
                Label restaurantNameLabel = (Label) cartCardNode.lookup("#cartResName");
                Button checkoutButton = (Button) cartCardNode.lookup("#cartResBtn");

                // Set the restaurant name on the label
                restaurantNameLabel.setText(cart.getRestaurant().getRestaurantName());

                // Add an event handler to the checkout button
                checkoutButton.setOnAction(event -> handleCheckoutClick(cart.getRestaurant().getId()));

                // Add the card to the container
                cartItemContainer.getChildren().add(cartCardNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void updateCart(){
        cartItemContainer.getChildren().clear();

        if (rid != 0) {
            try {
                loadCartItems();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            loadAllUserCartItems();
        }
    }
    public void renderAllCarts(){

    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getRid() {
        return rid;
    }

    // lisää tuotekortin ostoskorin käyttöliittymään
    private void addCartItemToUI(CartItem item) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/cartItem.fxml"));
        VBox cartItem = loader.load();


        CartItemController cartItemController = loader.getController();
        cartItemController.setCartItem(item);

        cartItem.setUserData(item);

        cartItemContainer.getChildren().add(cartItem);
    }


    // poista tuotekortin ostoskorin käyttöliittymästä
    private void removeCartItemFromUI(CartItem item) {
        cartItemContainer.getChildren().removeIf(node -> {
            CartItem associatedItem = (CartItem) node.getUserData();
            return associatedItem != null && associatedItem.getProduct().getProductID().equals(item.getProduct().getProductID());
        });
    }

    public void handleCheckoutClick(int rid) {
        System.out.println("Checkout clicked");
        try {
            System.out.println("RID: ccc " + rid);
            GoToCheckout(rid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addListeners() {
        System.out.println("Listener added");

        goToCheckout.setOnAction(event -> {
            handleCheckoutClick(rid);
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
        double subTotal = SessionManager.getInstance().getCart(rid).getTotalPrice();
        subTotalLabel.setText("Subtotal: " + subTotal);
    }

    @FXML
    private void GoToCheckout(int rid) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/checkout.fxml"));
        Parent root = loader.load();
        CheckoutController checkoutController = loader.getController();
        checkoutController.setRid(rid);
        mainController.setCenterContent(root);
    }
}
