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
    private int rid;

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
        System.out.println("Entered loadCartItems");
        ObservableList<CartItem> items = SessionManager.getInstance().getCart(rid).getItems();

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

    public void initializeCartForRestaurant(int restaurantId, Restaurant restaurant) {
        cartItemContainer.getChildren().clear();
        SessionManager sessionManager = SessionManager.getInstance();
        ShoppingCart cart = sessionManager.getCart(restaurantId);

        // If there is no cart for this restaurant, create a new one
        if (cart == null) {
            cart = new ShoppingCart(restaurant);
            sessionManager.addCart(restaurantId, cart);
            addCartListeners(cart);
        }
        try {
            loadCartItems();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // used to create ui elements to the ui when user adds or removes products to the cart
    private void addCartListeners(ShoppingCart cart) {
        ObservableList<CartItem> items = cart.getItems();
        System.out.println("Adding cart listeners");

        items.addListener((ListChangeListener<CartItem>) change -> {
            System.out.println("Cart items changed");
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
                    if (items.isEmpty()) {
                        displayEmptyCartMessage();
                    }
                }
            }

            updateSubTotal();
        });
    }

    // renders all the currently active carts for the user. used outside of restaurant and checkout views.
    public void loadAllUserCartItems() {

        HashMap<Integer, ShoppingCart> userCarts = SessionManager.getInstance().getCartMap();

        cartItemContainer.getChildren().clear();
        if (userCarts.isEmpty()) {
            displayEmptyCartMessage();
        } else {
            for (Map.Entry<Integer, ShoppingCart> entry : userCarts.entrySet()) {
                ShoppingCart cart = entry.getValue();
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/shoppingCarts.fxml"));
                    Node cartCardNode = loader.load();


                    Label restaurantNameLabel = (Label) cartCardNode.lookup("#cartResName");
                    Button checkoutButton = (Button) cartCardNode.lookup("#cartResBtn");


                    restaurantNameLabel.setText(cart.getRestaurant().getRestaurantName());


                    checkoutButton.setOnAction(event -> handleCheckoutClick(cart.getRestaurant().getId()));


                    cartItemContainer.getChildren().add(cartCardNode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //used to update which cart to show in the ui, if rid is 0, all carts are shown and not specific items.
    public void updateCart() {
        System.out.println("Updating cart. Current RID: " + rid);
        cartItemContainer.getChildren().clear();

        if (rid != 0) {
            try {
                loadCartItems();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                cartItemContainer.getChildren().clear();
                loadAllUserCartItems();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setRid(int rid) {
        this.rid = rid;
    }


    // lisää tuotekortin ostoskorin käyttöliittymään
    private void addCartItemToUI(CartItem item) throws IOException {
        System.out.println("Adding cart item to UI");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/cartItem.fxml"));
        VBox cartItem = loader.load();

        CartItemController cartItemController = loader.getController();
        cartItemController.setCartItem(item, cartItem);

        cartItem.setUserData(item);

        cartItemContainer.getChildren().add(cartItem);
    }


    //tekee mitä sanoo.
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
