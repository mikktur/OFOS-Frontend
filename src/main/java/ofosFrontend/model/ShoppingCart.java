package ofosFrontend.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Model class representing a shopping cart
 */
public class ShoppingCart {
    private final ObservableList<CartItem> items;
    private Restaurant restaurant;
    public ShoppingCart(Restaurant restaurant) {
        this.items = FXCollections.observableArrayList();
        this.restaurant = restaurant;
    }

    public void addItem(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getProductID().equals(product.getProductID())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(product, quantity, restaurant.getId()));
    }

    public void removeItem(Product product) {
        items.removeIf(item -> item.getProduct().getProductID().equals(product.getProductID()));
    }

    public ObservableList<CartItem> getItems() {
        return items;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }
    public void checkAndRemove(CartItem cartItem) {
        if (cartItem.getQuantity() <= 0) {
            removeItem(cartItem.getProduct());
        }
    }
    public void clear() {
        items.clear();
    }
}