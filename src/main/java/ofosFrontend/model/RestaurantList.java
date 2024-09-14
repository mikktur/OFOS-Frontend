package ofosFrontend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantList {
    private Restaurant restaurant;
    private List<Restaurant> restaurantList;

    public RestaurantList() {
        restaurantList = new ArrayList<>();
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

    public List<Restaurant> filterByCategory(String category) {
        return restaurantList.stream()
                .filter(restaurant -> restaurant.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());


    }
}
