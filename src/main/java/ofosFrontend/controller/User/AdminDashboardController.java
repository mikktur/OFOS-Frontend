package ofosFrontend.controller.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.User;
import ofosFrontend.service.RestaurantService;
import ofosFrontend.service.UserService;
import ofosFrontend.session.LocalizationManager;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashboardController {

    @FXML
    public Button banButton;
    @FXML
    public ComboBox<String> bannedUserSelector;

    @FXML
    private ListView<String> worker;

    @FXML
    private ComboBox<String> restaurantSelector;

    @FXML
    private ComboBox<String> userSelector;

    private final RestaurantService restaurantService = new RestaurantService();
    private final UserService userService = new UserService();

    ResourceBundle bundle = LocalizationManager.getBundle();

    @FXML
    public void initialize() {
        loadRestaurants();
        loadUsers();
        loadBannedUsers();

        // Add listeners
        restaurantSelector.setOnAction(event -> displaySelectedRestaurant());
        userSelector.setOnAction(event -> displaySelectedUser());
        bannedUserSelector.setOnAction(event -> displaySelectedUser());
    }

    private void loadUsers() {
        try {
            List<User> users = userService.getAllUsers();

            for (User user : users) {
                userSelector.getItems().add(user.getUsername());
            }

        } catch (IOException e) {
            showError(bundle.getString("Failed_to_load_user"));
            e.printStackTrace();
        }
    }

    private void loadBannedUsers() {
        try {
            List<User> users = userService.getAllUsers();

            for (User user : users) {
                // fix if-clause to (!user.getEnabled()) when backend is ready
                if (user.getEnabled() == null) {
                    bannedUserSelector.getItems().add(user.getUsername());
                }
            }

        } catch (IOException e) {
            showError(bundle.getString("Failed_to_load_user"));
            e.printStackTrace();
        }
    }

    private void loadRestaurants() {
        try {
            List<Restaurant> restaurants = restaurantService.getAllRestaurants();

            for (Restaurant restaurant : restaurants) {
                restaurantSelector.getItems().add(restaurant.getRestaurantName());
            }

        } catch (IOException e) {
            showError(bundle.getString("Failed_to_load_restaurants"));
            e.printStackTrace();
        }
    }

    private void displaySelectedRestaurant() {
        // Get the selected restaurant name
        String selectedRestaurantName = restaurantSelector.getValue();

        if (selectedRestaurantName != null) {
            // Clear the worker area and display the selected restaurant
            worker.getItems().clear();
            worker.getItems().add(bundle.getString("Selected_restaurant") + selectedRestaurantName);

        }
    }

    private void displaySelectedUser() {
        // Get the selected user
        String selectedUserName = userSelector.getValue();

        if (selectedUserName != null) {
            try {
                User selectedUser = userService.getUserByUsername(selectedUserName);

                // Clear the worker area and display the selected user
                worker.getItems().clear();
                worker.getItems().add(bundle.getString("Selected_user") + selectedUserName);
                worker.getItems().add(bundle.getString("User_ID") + selectedUser.getUserId());
                worker.getItems().add("Enabled: " + selectedUser.getEnabled());
            } catch (IOException e) {
                showError(bundle.getString("Failed_to_load_user"));
                e.printStackTrace();
            }
        }
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void banUser() {
        String selectedUserName = userSelector.getValue();
        if (selectedUserName != null) {
            try {
                User selectedUser = userService.getUserByUsername(selectedUserName);

                if (selectedUser != null) {
                    userService.banUser(selectedUser.getUserId());
                    worker.getItems().clear();
                    worker.getItems().add(bundle.getString("User_banned") + ": " + selectedUserName);
                }
            } catch (IOException e) {
                showError(bundle.getString("Failed_to_load_user"));
                e.printStackTrace();
            }
        }
        loadUsers();
    }
}
