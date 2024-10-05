package ofosFrontend.controller.Owner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Pair;
import ofosFrontend.model.Restaurant;
import ofosFrontend.service.RestaurantService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminMainMenuController extends AdminBasicController {
    @FXML
    private VBox restaurantsVBox; // VBox containing the list of restaurants
    @FXML
    private VBox restaurantListVBox;
    @FXML
    private Label addressLabel, phoneLabel, hoursLabel; // Labels for restaurant details
    @FXML
    private Text defaultText; // Default text to display when no restaurant is selected
    @FXML
    private Button editMenuButton; // Button to edit the menu of the selected restaurant
    private RestaurantService restaurantService = new RestaurantService();
    private Restaurant currentSelectedRestaurant; // Store the currently selected restaurant

    @FXML
    public void initialize() {

        loadRestaurants(); // Load restaurants when the UI initializes
    }
    public AdminMainMenuController() {
    }

    // Method to load the list of restaurants owned by the admin
    public void loadRestaurants() {
        try {

            restaurantListVBox.getChildren().clear();


            List<Restaurant> restaurants = restaurantService.getOwnerRestaurants();


            for (Restaurant restaurant : restaurants) {
                // Create an HBox for each restaurant entry
                HBox restaurantBox = new HBox();
                restaurantBox.setSpacing(10.0);
                restaurantBox.setStyle("-fx-padding: 5px; -fx-background-color: #e8f4fb; -fx-border-color: #000;");

                // Add the restaurant name to the HBox
                Text restaurantNameText = new Text(restaurant.getRestaurantName());
                restaurantNameText.setFont(Font.font(12));
                restaurantNameText.setStyle("-fx-font-weight: bold;");


                restaurantBox.getChildren().add(restaurantNameText);


                restaurantBox.setOnMouseClicked(event -> {

                    currentSelectedRestaurant = restaurant;


                    updateRestaurantDetailsUI();
                });


                restaurantListVBox.getChildren().add(restaurantBox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Unified method to update restaurant details in the UI
    private void updateRestaurantDetailsUI() {
        if (currentSelectedRestaurant != null) {
            defaultText.setText(currentSelectedRestaurant.getRestaurantName());
            addressLabel.setText(currentSelectedRestaurant.getAddress());
            phoneLabel.setText(currentSelectedRestaurant.getRestaurantPhone());
            hoursLabel.setText(currentSelectedRestaurant.getBusinessHours());
        }
    }

    @FXML
    private void modifyRestaurantInfo() {
        if (currentSelectedRestaurant == null) {
            showNoRestaurantSelectedAlert();
            return;
        }

        Optional<Pair<String, String>> result = showModifyRestaurantDialog();
        result.ifPresent(info -> handleDialogResult(info));
    }
    private void showNoRestaurantSelectedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Restaurant Selected");
        alert.setHeaderText("Please select a restaurant to modify.");
        alert.showAndWait();
    }
    private Optional<Pair<String, String>> showModifyRestaurantDialog() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Modify Restaurant Info");
        dialog.setHeaderText("Edit the contact information for the restaurant.");

        ButtonType modifyButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

        GridPane grid = createDialogGridPane();
        dialog.getDialogPane().setContent(grid);

        TextField addressField = new TextField(currentSelectedRestaurant.getAddress());
        TextField phoneField = new TextField(currentSelectedRestaurant.getRestaurantPhone());
        TextField hoursField = new TextField(currentSelectedRestaurant.getBusinessHours());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                return new Pair<>(addressField.getText(), phoneField.getText());
            }
            return null;
        });

        return dialog.showAndWait();
    }
    private GridPane createDialogGridPane() {
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

        return grid;
    }
    private void handleDialogResult(Pair<String, String> info) {
        updateRestaurantInfo(info);
        updateRestaurantDetailsUI();
        try {
            restaurantService.updateRestaurantInfo(currentSelectedRestaurant);
            showSuccessAlert(info);
        } catch (IOException e) {
            showErrorAlert(e);
        }
    }
    private void updateRestaurantInfo(Pair<String, String> info) {
        currentSelectedRestaurant.setAddress(info.getKey());
        currentSelectedRestaurant.setRestaurantPhone(info.getValue());
    }
    private void showSuccessAlert(Pair<String, String> info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Updated");
        alert.setHeaderText("Restaurant Information Updated Successfully");
        alert.setContentText("Address: " + info.getKey() +
                "\nPhone: " + info.getValue() +
                "\nBusiness Hours: " + currentSelectedRestaurant.getBusinessHours());
        alert.showAndWait();
    }
    private void showErrorAlert(IOException e) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("Failed to Update Restaurant Information");
        errorAlert.setContentText("An error occurred while updating the restaurant.");
        errorAlert.showAndWait();
        e.printStackTrace();
    }

    @FXML
    public void goToEditMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/Owner/adminFoodMenuUI.fxml"));

            Node root = loader.load();
            mainController.setCenterContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
