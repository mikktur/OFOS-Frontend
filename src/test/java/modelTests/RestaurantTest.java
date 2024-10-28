package modelTests;

import ofosFrontend.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantTest {

    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {
        // Initialize the restaurant with the parameterized constructor
        restaurant = new Restaurant("Sushi Place", "123-456-7890", "sushi.jpg");
    }

    @Test
    public void testDefaultConstructor() {
        // Test that the default constructor initializes fields to default values
        Restaurant defaultRestaurant = new Restaurant();
        assertEquals(0, defaultRestaurant.getId(), "Id should be 0 for the default constructor.");
        assertNull(defaultRestaurant.getRestaurantName(), "Restaurant name should be null for the default constructor.");
        assertNull(defaultRestaurant.getRestaurantPhone(), "Restaurant phone should be null for the default constructor.");
        assertNull(defaultRestaurant.getPicture(), "Picture should be null for the default constructor.");
    }

    @Test
    public void testParameterizedConstructor() {
        // Verify that the parameterized constructor sets the restaurantName, restaurantPhone, and picture correctly
        assertEquals("Sushi Place", restaurant.getRestaurantName(), "Restaurant name should be 'Sushi Place'.");
        assertEquals("123-456-7890", restaurant.getRestaurantPhone(), "Restaurant phone should be '123-456-7890'.");
        assertEquals("sushi.jpg", restaurant.getPicture(), "Picture should be 'sushi.jpg'.");
    }

    @Test
    public void testGetAndSetId() {
        restaurant.setId(101);
        assertEquals(101, restaurant.getId(), "Id should be set to 101.");
    }

    @Test
    public void testGetAndSetRestaurantName() {
        restaurant.setRestaurantName("Burger Joint");
        assertEquals("Burger Joint", restaurant.getRestaurantName(), "Restaurant name should be 'Burger Joint'.");
    }

    @Test
    public void testGetAndSetRestaurantPhone() {
        restaurant.setRestaurantPhone("987-654-3210");
        assertEquals("987-654-3210", restaurant.getRestaurantPhone(), "Restaurant phone should be '987-654-3210'.");
    }

    @Test
    public void testGetAndSetPicture() {
        restaurant.setPicture("burger.jpg");
        assertEquals("burger.jpg", restaurant.getPicture(), "Picture should be 'burger.jpg'.");
    }
}
