package ofosFrontend.session;

import ofosFrontend.model.ShoppingCart;

public class SessionManager {

    private static SessionManager session;
    private int userId;
    private String username;
    private String token;
    private ShoppingCart cart;
    private String role;

    private SessionManager() {

        username = null;
        userId = 0;
        token = null;
        cart = null;
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
        cart = null;
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

    public ShoppingCart getCart() {
        if (cart == null) {
            cart = new ShoppingCart();
        }
        return cart;
    }

    public void clearCart() {
        cart = null;
    }


}


