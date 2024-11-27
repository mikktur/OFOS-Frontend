package modelTests;

import ofosFrontend.model.Restaurant;
import ofosFrontend.model.RestaurantList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantListTest {

    private RestaurantList restaurantList;
    private Restaurant restaurant1;
    private Restaurant restaurant2;

    @BeforeEach
    void setUp() {
        // Initialize the RestaurantList and sample Restaurant objects before each test
        restaurantList = new RestaurantList();
        restaurant1 = new Restaurant("Pizza Place", "123-456-7890", "picture1.jpg", "seppo");
        restaurant2 = new Restaurant("Burger Joint", "987-654-3210", "picture2.jpg", "seppo");
    }

    @Test
    void testAddRestaurant() {
        restaurantList.addRestaurant(restaurant1);
        assertEquals(1, restaurantList.size(), "Restaurant list size should be 1 after adding one restaurant.");
    }

    @Test
    void testRemoveRestaurant() {
        restaurantList.addRestaurant(restaurant1);
        restaurantList.addRestaurant(restaurant2);
        restaurantList.removeRestaurant(restaurant1);
        assertEquals(1, restaurantList.size(), "Restaurant list size should be 1 after removing one restaurant.");
    }

    @Test
    void testClear() {
        restaurantList.addRestaurant(restaurant1);
        restaurantList.addRestaurant(restaurant2);
        restaurantList.clear();
        assertTrue(restaurantList.isEmpty(), "Restaurant list should be empty after clearing.");
    }

    @Test
    void testSize() {
        assertEquals(0, restaurantList.size(), "New restaurant list should have size 0.");
        restaurantList.addRestaurant(restaurant1);
        assertEquals(1, restaurantList.size(), "Restaurant list size should be 1 after adding a restaurant.");
    }

    @Test
    void testIsEmpty() {
        assertTrue(restaurantList.isEmpty(), "New restaurant list should be empty.");
        restaurantList.addRestaurant(restaurant1);
        assertFalse(restaurantList.isEmpty(), "Restaurant list should not be empty after adding a restaurant.");
    }

    @Test
    void testSetRestaurants() {
        List<Restaurant> newRestaurants = Arrays.asList(restaurant1, restaurant2);
        restaurantList.setRestaurants(newRestaurants);
        assertEquals(2, restaurantList.size(), "Restaurant list should have size 2 after setting a new list.");
    }
}
