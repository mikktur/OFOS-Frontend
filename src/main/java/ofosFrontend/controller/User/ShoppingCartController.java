package ofosFrontend.controller.User;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ofosFrontend.model.CartItem;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.ShoppingCart;
import ofosFrontend.session.CartManager;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * Controller for the shopping cart sidepanel
 */
public class ShoppingCartController extends BasicController {
    /**
     * The container for the cart items
     */
    @FXML
    private VBox cartItemContainer;
    /**
     * The label for the subtotal
     */
    @FXML
    private VBox cartCheckout;
    @FXML
    private Text subTotalLabel;
    /**
     * The button to go to checkout
     */
    @FXML
    private Button goToCheckout;
    /**
     * The root of the cart
     */
    @FXML
    private VBox cartRoot;
    //used to check if the listener has been initialized at least once. used in reloading the ui.
    private boolean listenerInitialized = false;

    /**
     * The restaurant id
     */
    private int rid;
    /**
     * The cart manager
     * @see CartManager
     */
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(LocalizationManager.getLocale());
    private CartManager cartManager = new CartManager();
    public ShoppingCartController() {

    }
    /**
     * Initializes the controller
     */
    @FXML
    public void initialize() {

        cartRoot.getProperties().put("controller", this);
        System.out.println("ShoppingCartController initialized");
        System.out.println("RID: " + rid);
        addListeners();
    }


    /**
     * Loads the cart items for the current restaurant
     * @throws IOException
     */
    public void loadCartItems() throws IOException {
        ObservableList<CartItem> items = cartManager.getCart(rid).getItems();

        cartItemContainer.getChildren().clear();
        if (items.isEmpty() ) {
            displayEmptyCartMessage();
        } else {
            for (CartItem item : items) {

                addCartItemToUI(item);
                addQuantityListener(item);
            }
            cartCheckout.setVisible(true);
        }
        updateSubTotal();
    }
    /**
     * Resets the cart view
     */
    public void resetCartView() {
        //magic number 0 is used as rid to indicate no restaurant is selected
        setRid(0);
        cartManager.checkAndRemoveEmptyCarts();
        cartCheckout.setVisible(false);
        updateCart();
    }

    /**
     * Initializes the cart for a specific restaurant
     * @param restaurantId the id of the restaurant
     * @param restaurant the restaurant
     */
    public void initializeCartForRestaurant(int restaurantId, Restaurant restaurant) {

        cartItemContainer.getChildren().clear();
        rid = restaurantId;
        ShoppingCart cart = cartManager.getOrCreateCart(restaurantId, restaurant);
        if (cart.getItems().isEmpty() || !listenerInitialized) {
            addCartListeners(cart);
            listenerInitialized=true;
        }
        try {
            loadCartItems();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // used to add ui elements to the ui when user adds or removes products to the cart

    /**
     * Adds listeners to the cart
     * @param cart the cart to add listeners to
     *             @see ShoppingCart
     */
    private void addCartListeners(ShoppingCart cart) {
        ObservableList<CartItem> items = cart.getItems();
        System.out.println("Adding cart listeners");

        items.addListener((ListChangeListener<CartItem>) change -> {
            System.out.println("Cart items changed");
            while (change.next()) {
                if (change.wasAdded()) {
                    removeEmptyCartMessage();
                    cartCheckout.setVisible(true);
                    for (CartItem addedItem : change.getAddedSubList()) {
                        try {
                            mainController.showRedDot();
                            addCartItemToUI(addedItem);
                            addQuantityListener(addedItem);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (change.wasRemoved()) {
                    if (items.isEmpty()) {
                        mainController.hideRedDot();
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
            mainController.hideRedDot();
        } else {
            for (Map.Entry<Integer, ShoppingCart> entry : userCarts.entrySet()) {
                ShoppingCart cart = entry.getValue();
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/shoppingCarts.fxml"));
                    Node cartCardNode = loader.load();


                    Label restaurantNameLabel = (Label) cartCardNode.lookup("#cartResName");
                    Button checkoutButton = (Button) cartCardNode.lookup("#cartResBtn");


                    restaurantNameLabel.setText(cart.getRestaurant().getRestaurantName());
                    Label cartSum = (Label) cartCardNode.lookup("#cartSum");
                    cartSum.setText(String.valueOf(cart.getTotalPrice()));

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
                cartCheckout.setVisible(true);
                loadCartItems();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                cartCheckout.setVisible(false);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/cartItem.fxml"));
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
        cartCheckout.setVisible(false);
        ResourceBundle bundle = LocalizationManager.getBundle();
        Label emptyItem = new Label(bundle.getString("emptyCart"));
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


        subTotalLabel.setText(currencyFormatter.format(subTotal));
    }

    @FXML
    private void GoToCheckout(int rid) throws IOException {
        setRid(rid);
        updateCart();
        mainController.loadCheckoutView(rid);
    }
}
