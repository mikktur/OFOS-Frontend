package ofosFrontend.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import ofosFrontend.session.SessionManager;

public class CartItem {
    private final Product product;
    private final IntegerProperty quantity;
    private int rid = 0;
    // Constructor
    public CartItem(Product product, int quantity, int rid) {
        this.product = product;
        this.quantity = new SimpleIntegerProperty(quantity);
        this.rid = rid;
    }

    public Product getProduct() {
        return product;
    }

    public int getRid() {
        return rid;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public double getTotalPrice() {
        return product.getProductPrice() * getQuantity();
    }

    public void addQuantity() {
        quantity.set(quantity.get() + 1);
    }

    public void subQuantity() {
        if (quantity.get() > 1) {
            quantity.set(quantity.get() - 1);

        }
    }
}