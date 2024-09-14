package ofosFrontend.session;

import ofosFrontend.model.ShoppingCart;

public class SessionManager {

    private static SessionManager session;
    private String username;
    private String token;
    private ShoppingCart cart;

    private SessionManager() {
        username = null;
        token = null;
        cart = null;
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

    public String getUsername() {
        return username != null ? username : "";
    }

    public String getToken() {
        return token != null ? token : "";
    }

    public boolean isLoggedIn() {
        return token != null;
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


