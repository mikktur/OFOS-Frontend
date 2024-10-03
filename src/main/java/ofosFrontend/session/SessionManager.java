package ofosFrontend.session;

import ofosFrontend.model.Restaurant;
import ofosFrontend.model.ShoppingCart;

import java.util.HashMap;

public class SessionManager {

    private static SessionManager session;
    private int userId;
    private String username;
    private String token;
    private final HashMap<Integer, ShoppingCart> cartMap;
    private String role;

    private SessionManager() {
        cartMap = new HashMap<>();
        username = null;
        userId = 0;
        token = null;
        role = null;
    }

    public static SessionManager getInstance() {
        if (session == null) {
            synchronized (SessionManager.class) {
                if (session == null) {
                    session = new SessionManager();
                }
            }
        }
        return session;
    }

    public void login(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public void logout() {
        username = null;
        token = null;

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

    public void addCart(int restaurantId, ShoppingCart cart) {
        cartMap.put(restaurantId, cart);
    }
    public HashMap<Integer, ShoppingCart> getCartMap() {
        return cartMap;
    }
    public void removeCart(int id) {
        cartMap.remove(userId);
    }
    public ShoppingCart getCart(int restaurantId) {
        return cartMap.get(restaurantId);
    }

    public void clearAllCarts() {
        cartMap.clear();
    }


}


