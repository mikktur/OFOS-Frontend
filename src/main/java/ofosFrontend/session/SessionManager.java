package ofosFrontend.session;

import ofosFrontend.model.ShoppingCart;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the session for the user.
 * Stores the user's information and shopping carts.
 */
public class SessionManager {

    private static SessionManager session;
    private int userId;
    private String username;
    private String token;
    private final Map<Integer, ShoppingCart> cartMap;
    private String role;

    private SessionManager() {
        cartMap = new HashMap<>();
        username = null;
        userId = 0;
        token = null;
        role = null;
    }
    /**
     * Gets the instance of the session manager
     * @return The session manager
     */
    public static synchronized SessionManager getInstance() {
        if (session == null) {
            session = new SessionManager();
        }
        return session;
    }

    public void login(String username, String token) {
        this.username = username;
        this.token = token;
    }

    /**
     * Upon logout, clears all the session data.
     */
    public void logout() {
        username = null;
        token = null;
        userId = 0;
        role = null;
        cartMap.clear();

    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username != null ? username : "";
    }

    public String getToken() {
        return token != null ? token : "";
    }

    public boolean isLoggedIn() {
        return token != null;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    /**
     * Adds a cart to the session
     * @param restaurantId The ID of the restaurant
     * @param cart The cart to add
     */
    public void addCart(int restaurantId, ShoppingCart cart) {
        cartMap.put(restaurantId, cart);
    }

    /**
     * Gets the cart map
     * @return The cart map
     */
    public Map<Integer, ShoppingCart> getCartMap() {
        return cartMap;
    }

    /**
     * Removes a cart from the session
     * @param id The ID of the restaurant
     */
    public void removeCart(int id) {
        cartMap.remove(id);
    }

    /**
     * Gets a cart from the session
     * @param restaurantId The ID of the restaurant
     * @return The cart
     */
    public ShoppingCart getCart(int restaurantId) {
        return cartMap.get(restaurantId);
    }

    public void clearAllCarts() {
        cartMap.clear();
    }



}


