package ofosFrontend.model;

import java.util.ArrayList;
import java.util.List;


public class ShoppingCart {
    private List<CartItem> items;

    public ShoppingCart() {
        items = new ArrayList<>();
    }

    public void addItem(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public void removeItem(Product product) {
        items.removeIf(item -> item.getProduct().getProductId().equals(product.getProductId()));
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    public void clear() {
        items.clear();
    }
}