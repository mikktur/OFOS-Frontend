package ofosFrontend.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Class to represent a cart item in the shopping cart
 * @see ShoppingCart
 */
public class CartItem {
    private final Product product;
    private final IntegerProperty quantity;
    private int rid;
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
        if (quantity.get() > 0) {
            quantity.set(quantity.get() - 1);

        }
    }
}