package modelTests;

import javafx.collections.ObservableList;
import ofosFrontend.model.CartItem;
import ofosFrontend.model.Product;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartTest {

    private ShoppingCart shoppingCart;
    private Product product1;
    private Product product2;

    @BeforeEach
    public void setUp() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1);
        shoppingCart = new ShoppingCart(restaurant);

        product1 = new Product("Laptop", 999.99, "High-performance laptop", 12345, "laptop.jpg", "Electronics", true);
        product2 = new Product("Smartphone", 599.99, "Latest smartphone", 54321, "smartphone.jpg", "Electronics", true);
    }

    @Test
    public void testAddNewItem() {
        shoppingCart.addItem(product1, 1);

        ObservableList<CartItem> items = shoppingCart.getItems();
        assertEquals(1, items.size(), "Shopping cart should have 1 item.");
        assertEquals(product1, items.get(0).getProduct(), "The product added should be 'Laptop'.");
        assertEquals(1, items.get(0).getQuantity(), "The quantity should be 1.");
    }

    @Test
    public void testAddExistingItemIncreasesQuantity() {
        shoppingCart.addItem(product1, 1);
        shoppingCart.addItem(product1, 2);

        ObservableList<CartItem> items = shoppingCart.getItems();
        assertEquals(1, items.size(), "Shopping cart should have 1 item.");
        assertEquals(3, items.get(0).getQuantity(), "The quantity should be updated to 3.");
    }

    @Test
    public void testAddDifferentItems() {
        shoppingCart.addItem(product1, 1);
        shoppingCart.addItem(product2, 1);

        ObservableList<CartItem> items = shoppingCart.getItems();
        assertEquals(2, items.size(), "Shopping cart should have 2 items.");
        assertEquals(product1, items.get(0).getProduct(), "First product should be 'Laptop'.");
        assertEquals(product2, items.get(1).getProduct(), "Second product should be 'Smartphone'.");
    }

    @Test
    public void testRemoveItem() {
        shoppingCart.addItem(product1, 1);
        shoppingCart.removeItem(product1);

        ObservableList<CartItem> items = shoppingCart.getItems();
        assertTrue(items.isEmpty(), "Shopping cart should be empty after removing the item.");
    }

    @Test
    public void testGetTotalPrice() {
        shoppingCart.addItem(product1, 2);
        shoppingCart.addItem(product2, 3);

        double totalPrice = shoppingCart.getTotalPrice();
        double expectedTotal = (2 * 999.99) + (3 * 599.99);

        assertEquals(expectedTotal, totalPrice, 0.001, "The total price should be the sum of all item prices.");
    }

    @Test
    public void testClearCart() {
        shoppingCart.addItem(product1, 1);
        shoppingCart.addItem(product2, 1);
        shoppingCart.clear();

        ObservableList<CartItem> items = shoppingCart.getItems();
        assertTrue(items.isEmpty(), "Shopping cart should be empty after clearing.");
    }
}
