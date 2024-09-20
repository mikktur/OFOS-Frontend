package ofosFrontend.model;

import ofosFrontend.session.SessionManager;

public class CartItem {
    private Product product;
    private int quantity;

    // Constructor
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }


    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return product.getProductPrice() * quantity;
    }

    public void addQuantity() {
        quantity++;
    }

    public void subQuantity() {
        if (quantity > 1) {
            quantity--;
        } else {
            SessionManager sessionManager = SessionManager.getInstance();
            sessionManager.getCart().removeItem(product);
        }
    }
}