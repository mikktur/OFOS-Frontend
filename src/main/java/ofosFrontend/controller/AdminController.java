package ofosFrontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
    private VBox restaurantListVBox; // VBox containing the restaurants list
    @FXML
    private Label addressLabel, phoneLabel, hoursLabel; // Labels for restaurant details
    @FXML
    private Text defaultText; // Text for displaying the selected restaurant name
    @FXML
    private ImageView adminLogout; // Logout button

    private RestaurantService restaurantService = new RestaurantService();

    @FXML
    public void initialize() {
        loadRestaurants(); // Load restaurants when the UI initializes
    }

    // Method to load the list of restaurants owned by the admin
    public void loadRestaurants() {
        try {
            // Fetch the list of restaurants from the service
            List<Restaurant> restaurants = restaurantService.getOwnerRestaurants();

            // Clear the VBox before adding new restaurant entries
            restaurantListVBox.getChildren().clear();

            // Iterate through the list of restaurants
            for (Restaurant restaurant : restaurants) {
                // Create an HBox for each restaurant entry
                HBox restaurantBox = new HBox();
                restaurantBox.setSpacing(10.0);
                restaurantBox.setStyle("-fx-padding: 5px; -fx-background-color: #e8f4fb; -fx-border-color: #000;");

                // Create a Text node for the restaurant name
                Text restaurantNameText = new Text(restaurant.getRestaurantName());
                restaurantNameText.setFont(Font.font(14));  // Set smaller font size
                restaurantNameText.setStyle("-fx-font-weight: bold;");

                // Add the restaurant name to the HBox
                restaurantBox.getChildren().add(restaurantNameText);

                // Add click event to load the selected restaurant's details
                restaurantBox.setOnMouseClicked(event -> {
                    updateRestaurantDetails(restaurant);
                });

                // Add the HBox to the VBox (restaurantListVBox)
                restaurantListVBox.getChildren().add(restaurantBox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateRestaurantDetails(Restaurant restaurant) {
        // Update the labels with the restaurant's details
        defaultText.setText(restaurant.getRestaurantName()); // Update the "Default restaurant" text
        addressLabel.setText(restaurant.getAddress()); // Update the address label
        phoneLabel.setText(restaurant.getRestaurantPhone()); // Update the phone label
        hoursLabel.setText(restaurant.getBusinessHours()); // Update the business hours label
    }

    @FXML
    public void goToEditMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/AdminFoodMenuUI.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Admin Food Menu");

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to modify restaurant information
    @FXML
    private void modifyRestaurantInfo() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Modify Restaurant Info");
        dialog.setHeaderText("Edit the contact information for the restaurant.");

        ButtonType modifyButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        addressField.setText(addressLabel.getText());

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        phoneField.setText(phoneLabel.getText());

        TextField hoursField = new TextField();
        hoursField.setPromptText("Business Hours");
        hoursField.setText(hoursLabel.getText());

        grid.add(new Label("Address:"), 0, 0);
        grid.add(addressField, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Business Hours:"), 0, 2);
        grid.add(hoursField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                return new Pair<>(addressField.getText(), phoneField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(info -> {
            addressLabel.setText(addressField.getText());
            phoneLabel.setText(phoneField.getText());
            hoursLabel.setText(hoursField.getText());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Updated");
            alert.setHeaderText("Restaurant Information Updated Successfully");
            alert.setContentText("Address: " + addressField.getText() +
                    "\nPhone: " + phoneField.getText() +
                    "\nBusiness Hours: " + hoursField.getText());
            alert.showAndWait();
        });
    }

    @FXML
    public void logOut(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/loginUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) adminLogout.getScene().getWindow();
        Scene backToLoginScene = new Scene(root, 650, 400);
        currentStage.setTitle("OFOS Login");
        currentStage.setScene(backToLoginScene);
        currentStage.show();
    }
}
