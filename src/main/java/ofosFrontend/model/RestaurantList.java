package ofosFrontend.model;

import ofosFrontend.service.RestaurantService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class RestaurantList {
    private Restaurant restaurant;
    private List<Restaurant> restaurantsList;
    private RestaurantService restaurantService;
    private static final Logger logger = LogManager.getLogger(RestaurantList.class);
    public RestaurantList() {
        restaurantService = new RestaurantService();
        try {
            restaurantsList = restaurantService.getAllRestaurants();
        } catch (IOException e) {
            logger.error("Failed to get all restaurants: {}", e.getMessage());
        }
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<Restaurant> getRestaurantsList() {
        return restaurantsList;
    }
    public void setRestaurants(List<Restaurant> restaurantList) {
        this.restaurantsList = restaurantList;
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurantsList.add(restaurant);
    }

    public void removeRestaurant(Restaurant restaurant) {
        restaurantsList.remove(restaurant);
    }

    public void clear() {
        restaurantsList.clear();
    }

    public int size() {
        return restaurantsList.size();
    }

    public boolean isEmpty() {
        return restaurantsList.isEmpty();
    }

    public List<Restaurant> filterByCategory(String category) throws IOException {

        restaurantsList = restaurantService.getRestaurantsByCategory(category);
        return restaurantsList;
   }
}
