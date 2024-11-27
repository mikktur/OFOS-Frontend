package modelTests;

import ofosFrontend.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    public void setUp() {
        // Initialize the product with the parameterized constructor
        product = new Product("Laptop", 999.99, "High-performance laptop", 12345, "laptop.jpg", "Electronics", true);
    }

    @Test
    void testDefaultConstructor() {
        // Test that the default constructor initializes fields to default values
        Product defaultProduct = new Product();
        assertNull(defaultProduct.getProductName(), "Product name should be null for the default constructor.");
        assertEquals(0.0, defaultProduct.getProductPrice(), 0.001, "Product price should be 0.0 for the default constructor.");
        assertNull(defaultProduct.getProductDesc(), "Product description should be null for the default constructor.");
        assertNull(defaultProduct.getProductID(), "Product ID should be null for the default constructor.");
        assertNull(defaultProduct.getPicture(), "Product picture should be null for the default constructor.");
        assertNull(defaultProduct.getCategory(), "Product category should be null for the default constructor.");
        assertFalse(defaultProduct.isActive(), "Product should be        assertFalse(defaultProduct.isActive(), Product should be inactive by default.");
    }

    @Test
    void testParameterizedConstructor() {
        assertEquals("Laptop", product.getProductName(), "Product name should be 'Laptop'.");
        assertEquals(999.99, product.getProductPrice(), 0.001, "Product price should be 999.99.");
        assertEquals("High-performance laptop", product.getProductDesc(), "Product description should be 'High-performance laptop'.");
        assertEquals(12345, product.getProductID(), "Product ID should be 12345.");
        assertEquals("laptop.jpg", product.getPicture(), "Product picture should be 'laptop.jpg'.");
        assertEquals("Electronics", product.getCategory(), "Product category should be 'Electronics'.");
        assertTrue(product.isActive(), "Product should be active.");
    }

    @Test
    void testGetAndSetProductName() {
        product.setProductName("Smartphone");
        assertEquals("Smartphone", product.getProductName(), "Product name should be 'Smartphone'.");
    }

    @Test
    void testGetAndSetProductPrice() {
        product.setProductPrice(599.99);
        assertEquals(599.99, product.getProductPrice(), 0.001, "Product price should be 599.99.");
    }

    @Test
    void testGetAndSetProductDesc() {
        product.setProductDesc("Latest smartphone model");
        assertEquals("Latest smartphone model", product.getProductDesc(), "Product description should be 'Latest smartphone model'.");
    }

    @Test
    void testGetAndSetProductID() {
        product.setProductID(54321);
        assertEquals(54321, product.getProductID(), "Product ID should be 54321.");
    }

    @Test
    void testGetAndSetPicture() {
        product.setPicture("smartphone.jpg");
        assertEquals("smartphone.jpg", product.getPicture(), "Product picture should be 'smartphone.jpg'.");
    }

    @Test
    void testGetAndSetCategory() {
        product.setCategory("Mobile Phones");
        assertEquals("Mobile Phones", product.getCategory(), "Product category should be 'Mobile Phones'.");
    }

    @Test
    void testGetAndSetActive() {
        product.setActive(false);
        assertFalse(product.isActive(), "Product should be inactive.");

        product.setActive(true);
        assertTrue(product.isActive(), "Product should be active.");
    }

    @Test
    void testToString() {
        String expectedString = "Product{productName='Laptop', price=999.99, productDesc='High-performance laptop', " +
                "productID=12345, picture='laptop.jpg', category='Electronics', active=true}";
        assertEquals(expectedString, product.toString(), "toString() method should return the expected string representation.");
    }
}

