package ofosFrontend.model;

import ofosFrontend.service.RestaurantService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a list of restaurants
 */
public class RestaurantList {
    private Restaurant restaurant;
    private List<Restaurant> restaurantList;
    private RestaurantService restaurantService;
    private static final Logger logger = LogManager.getLogger(RestaurantList.class);


    public RestaurantList(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
        try {
            this.restaurantList = new ArrayList<>(restaurantService.getAllRestaurants());
        } catch (IOException e) {
            e.printStackTrace();
            this.restaurantList = new ArrayList<>(); // Fallback to an empty mutable list
            logger.error("Failed to get all restaurants: {}", e.getMessage());
        }
    }


    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }
    public void setRestaurants(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }
    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurantList.add(restaurant);
    }

    public void removeRestaurant(Restaurant restaurant) {
        restaurantList.remove(restaurant);
    }

    public void clear() {
        restaurantList.clear();
    }

    public int size() {
        return restaurantList.size();
    }

    public boolean isEmpty() {
        return restaurantList.isEmpty();
    }

    public void getNames() {
        restaurantList.forEach(restaurant1 -> System.out.println(restaurant1.getRestaurantName()));
    }
    public List<Restaurant> filterByCategory(String category) throws IOException {

        restaurantList = restaurantService.getRestaurantsByCategory(category);
        return restaurantList;
   }
}
