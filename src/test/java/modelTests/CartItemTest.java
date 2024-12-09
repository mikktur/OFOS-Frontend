package modelTests;

import ofosFrontend.model.CartItem;
import ofosFrontend.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    private CartItem cartItem;
    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();

        cartItem = new CartItem(product, 5,1);

    }

    @Test
    void testGetProduct() {
        assertEquals(product, cartItem.getProduct(), "CartItem should return the correct product.");
    }

    @Test
    void testGetQuantity() {
        assertEquals(5, cartItem.getQuantity(), "Initial quantity should be 5.");
    }

    @Test
    void testSetQuantity() {
        cartItem.setQuantity(3);
        assertEquals(3, cartItem.getQuantity(), "Quantity should be updated to 3.");
    }

    @Test
    void testAddQuantity() {
        cartItem.addQuantity();
        assertEquals(6, cartItem.getQuantity(), "Quantity should be incremented by 1 (5 + 1 = 6).");
    }

    @Test
    void testSubQuantity_DecreasesQuantity() {
        cartItem.subQuantity();
        assertEquals(4, cartItem.getQuantity(), "Quantity should be decremented by 1 (5 - 1 = 4).");
    }
}