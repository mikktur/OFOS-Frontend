package ofosFrontend.controller.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.User;
import ofosFrontend.service.RestaurantService;
import ofosFrontend.service.UserService;

import java.io.IOException;
import java.util.List;

public class AdminDashboardController {

    @FXML
    private ComboBox<String> restaurantSelector;

    @FXML
    private ComboBox<String> userSelector;

    private final RestaurantService restaurantService = new RestaurantService();
    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        loadRestaurants();
        loadUsers();
    }

    private void loadUsers() {
        try {
            // Fetch the list of users
            List<User> users = userService.getAllUsers();

            // Populate the ComboBox with user names
            for (User user : users) {
                userSelector.getItems().add(user.getUsername());
            }

        } catch (IOException e) {
            // Handle exception and show an error alert
            showErrorAlert("Failed to load users", "Could not fetch user data. Please try again later.");
            e.printStackTrace();
        }
    }

    public void handleEditRestaurant(ActionEvent actionEvent) {
    }

    public void handleEditUser(ActionEvent actionEvent) {
    }

    public void handleEditProduct(ActionEvent actionEvent) {
    }

    private void loadRestaurants() {
        try {
            // Fetch the list of restaurants
            List<Restaurant> restaurants = restaurantService.getAllRestaurants();

            // Populate the ComboBox with restaurant names
            for (Restaurant restaurant : restaurants) {
                restaurantSelector.getItems().add(restaurant.getRestaurantName());
            }

        } catch (IOException e) {
            // Handle exception and show an error alert
            showErrorAlert("Failed to load restaurants", "Could not fetch restaurant data. Please try again later.");
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
