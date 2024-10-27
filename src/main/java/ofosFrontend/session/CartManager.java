package ofosFrontend.session;

import ofosFrontend.model.Restaurant;
import ofosFrontend.model.ShoppingCart;

import java.util.Iterator;
import java.util.Map;

/**
 * Manages the shopping carts for the user
 * @see ShoppingCart
 * @see SessionManager
 */

// someday im gonna finish this
public class CartManager {
    private final SessionManager sessionManager;

    public CartManager() {
        this.sessionManager = SessionManager.getInstance();
    }


    public void addCart(int restaurantId, ShoppingCart cart) {
        sessionManager.getCartMap().put(restaurantId, cart);
    }
    public ShoppingCart getCart(int restaurantId) {
        return sessionManager.getCartMap().get(restaurantId);
    }

    public ShoppingCart getOrCreateCart(int restaurantId, Restaurant restaurant) {
        ShoppingCart cart = sessionManager.getCartMap().get(restaurantId);
        if (cart == null) {
            // Create a new cart if none exists for the given restaurant ID
            cart = new ShoppingCart(restaurant);
            addCart(restaurantId, cart);
        }
        return cart;
    }


    public void removeCart(int restaurantId) {
        sessionManager.getCartMap().remove(restaurantId);
    }


    public void clearAllCarts() {
        sessionManager.getCartMap().clear();
    }

    // Checks and removes empty carts
    public void checkAndRemoveEmptyCarts() {
        Iterator<Map.Entry<Integer, ShoppingCart>> iterator = sessionManager.getCartMap().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, ShoppingCart> entry = iterator.next();
            ShoppingCart cart = entry.getValue();
            if (cart.getItems().isEmpty()) {
                iterator.remove();
            }
        }
    }
}