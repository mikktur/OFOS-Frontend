package ofosFrontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import ofosFrontend.model.Restaurant;
import ofosFrontend.service.RestaurantService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminController {
    @FXML
    private VBox restaurantsVBox; // VBox containing the list of restaurants
    @FXML
    private VBox restaurantListVBox;
    @FXML
    private Label addressLabel, phoneLabel, hoursLabel; // Labels for restaurant details
    @FXML
    private ImageView adminLogout; // Logout button
    @FXML
    private Text defaultText; // Default text to display when no restaurant is selected
    private int rID;

    private RestaurantService restaurantService = new RestaurantService();
    private Restaurant currentSelectedRestaurant; // Store the currently selected restaurant

    @FXML
    public void initialize() {
        loadRestaurants(); // Load restaurants when the UI initializes
    }

    // Method to load the list of restaurants owned by the admin
    public void loadRestaurants() {
        try {
            // Clear the VBox before adding new restaurant entries
            restaurantListVBox.getChildren().clear();

            // Fetch the list of restaurants from the service
            List<Restaurant> restaurants = restaurantService.getOwnerRestaurants();

            // Iterate through the list of restaurants
            for (Restaurant restaurant : restaurants) {
                // Create an HBox for each restaurant entry
                HBox restaurantBox = new HBox();
                restaurantBox.setSpacing(10.0);
                restaurantBox.setStyle("-fx-padding: 5px; -fx-background-color: #e8f4fb; -fx-border-color: #000;");

                // Create a Text node for the restaurant name
                Text restaurantNameText = new Text(restaurant.getRestaurantName());
                restaurantNameText.setFont(Font.font(12));  // Adjust font size to be smaller
                restaurantNameText.setStyle("-fx-font-weight: bold;");

                // Add the restaurant name to the HBox
                restaurantBox.getChildren().add(restaurantNameText);

                // Add click event to load the selected restaurant's details
                restaurantBox.setOnMouseClicked(event -> {
                    rID = restaurant.getId();
                    currentSelectedRestaurant = restaurant;

                    // Update the UI with the selected restaurant's details
                    updateRestaurantDetailsUI();
                });

                // Add the HBox to the VBox (restaurantListVBox)
                restaurantListVBox.getChildren().add(restaurantBox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Unified method to update restaurant details in the UI
    private void updateRestaurantDetailsUI() {
        if (currentSelectedRestaurant != null) {
            // Update the labels in the UI with the selected restaurant's details
            defaultText.setText(currentSelectedRestaurant.getRestaurantName());
            addressLabel.setText(currentSelectedRestaurant.getAddress());
            phoneLabel.setText(currentSelectedRestaurant.getRestaurantPhone());
            hoursLabel.setText(currentSelectedRestaurant.getBusinessHours());
        }
    }

    // Method to modify restaurant information
    @FXML
    private void modifyRestaurantInfo() {
        if (currentSelectedRestaurant == null) {
            // If no restaurant is selected, show an error message
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Restaurant Selected");
            alert.setHeaderText("Please select a restaurant to modify.");
            alert.showAndWait();
            return;
        }

        // Proceed with modifying the selected restaurant
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Modify Restaurant Info");
        dialog.setHeaderText("Edit the contact information for the restaurant.");

        ButtonType modifyButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField addressField = new TextField(currentSelectedRestaurant.getAddress());
        TextField phoneField = new TextField(currentSelectedRestaurant.getRestaurantPhone());
        TextField hoursField = new TextField(currentSelectedRestaurant.getBusinessHours());

        grid.add(new Label("Address:"), 0, 0);
        grid.add(addressField, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Business Hours:"), 0, 2);
        grid.add(hoursField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Convert result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                return new Pair<>(addressField.getText(), phoneField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(info -> {
            // Update the selected restaurant's details
            currentSelectedRestaurant.setAddress(addressField.getText());
            currentSelectedRestaurant.setRestaurantPhone(phoneField.getText());
            currentSelectedRestaurant.setHours(hoursField.getText());

            // Update the UI with new data
            updateRestaurantDetailsUI();

            // Now update the backend
            try {
                restaurantService.updateRestaurantInfo(currentSelectedRestaurant);

                // Show success alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Updated");
                alert.setHeaderText("Restaurant Information Updated Successfully");
                alert.setContentText("Address: " + addressField.getText() +
                        "\nPhone: " + phoneField.getText() +
                        "\nBusiness Hours: " + hoursField.getText());
                alert.showAndWait();
            } catch (IOException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Failed to Update Restaurant Information");
                errorAlert.setContentText("An error occurred while updating the restaurant.");
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void logOut(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/loginUI.fxml"));
            Parent root = loader.load();
            System.out.println("Logged out, I guess");

            Stage currentStage = (Stage) adminLogout.getScene().getWindow();
            Scene backToLoginScene = new Scene(root, 650, 400);
            currentStage.setTitle("OFOS Login");
            currentStage.setScene(backToLoginScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToEditMenu(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/AdminFoodMenuUI.fxml"));
            AdminMenuController controller = loader.getController();
            controller.setRId(rID);
            Parent root = loader.load();

            Stage currentStage = (Stage) adminLogout.getScene().getWindow();
            Scene editMenuScene = new Scene(root, 650, 400);
            currentStage.setTitle("OFOS Admin");
            currentStage.setScene(editMenuScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
