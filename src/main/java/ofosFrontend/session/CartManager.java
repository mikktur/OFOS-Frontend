package ofosFrontend.session;

import ofosFrontend.model.Product;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.ShoppingCart;
import ofosFrontend.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Manages the shopping carts for the user
 *
 * @see ShoppingCart
 * @see SessionManager
 */

// someday im gonna finish this
public class CartManager {
    private final SessionManager sessionManager;
    private static final Logger logger = LogManager.getLogger(CartManager.class);

    public CartManager() {
        this.sessionManager = SessionManager.getInstance();
    }

    /**
     * Adds a cart to the session
     *
     * @param restaurantId The ID of the restaurant
     * @param cart         The cart to add
     */
    public void addCart(int restaurantId, ShoppingCart cart) {
        sessionManager.getCartMap().put(restaurantId, cart);
    }

    /**
     * Gets a cart from the session
     *
     * @param restaurantId The ID of the restaurant
     * @return The cart
     */
    public ShoppingCart getCart(int restaurantId) {
        ShoppingCart cart = sessionManager.getCartMap().get(restaurantId);

        if (cart != null) {
            updateCartWithLocalizedProducts(cart);
        }
        return cart;
    }
    public Map<Integer, ShoppingCart> getCartMap() {
        Map <Integer, ShoppingCart> cartMap = sessionManager.getCartMap();
        cartMap.forEach((restaurantId, cart) -> updateCartWithLocalizedProducts(cart));
        return cartMap;
    }
    private void updateCartWithLocalizedProducts(ShoppingCart cart) {
        ProductService productService = new ProductService();
        String lang = LocalizationManager.getLanguageCode();

        cart.getItems().forEach(cartItem -> {
            try {
                if (cartItem.getProduct() == null) {
                    return;
                }
                // Fetch product details for the current language
                Product updatedProduct = productService.getProductByIdAndLang(cartItem.getProduct().getProductID(), lang);

                // Update the cart item with the localized product details
                cartItem.setProduct(updatedProduct);
            } catch (IOException e) {
                logger.error("Error fetching localized product details for product ID: {}", cartItem.getProduct().getProductID(), e);
            }
        });
    }
    /**
     * Gets or creates a cart for the given restaurant ID
     *
     * @param restaurantId The ID of the restaurant
     * @param restaurant   The restaurant object
     * @return The cart
     */
    public ShoppingCart getOrCreateCart(int restaurantId, Restaurant restaurant) {
        ShoppingCart cart = sessionManager.getCartMap().get(restaurantId);
        if (cart == null) {
            // Create a new cart if none exists for the given restaurant ID
            cart = new ShoppingCart(restaurant);
            addCart(restaurantId, cart);
        }
        updateCartWithLocalizedProducts(cart);
        return cart;
    }

    /**
     * Removes a cart from the session
     *
     * @param restaurantId The ID of the restaurant
     */
    public void removeCart(int restaurantId) {
        sessionManager.getCartMap().remove(restaurantId);
    }

    /**
     * Clears all carts from the session
     */
    public void clearAllCarts() {
        sessionManager.getCartMap().clear();
    }


    /**
     * Checks all carts and removes the ones that are empty
     */
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