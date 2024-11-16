package ofosFrontend.controller.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.User;
import ofosFrontend.service.RestaurantService;
import ofosFrontend.service.UserService;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.TextFieldUtils;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class AddRestaurantDialogController {

    public TextField addressField;
    public TextField hoursField;
    public TextField nameField;
    public TextField phoneField;
    public TextField pictureURL;
    @FXML
    public ComboBox<String> dialogOwnerSelector;
    private static final int STREET_ADDRESS_MAX_LENGTH = 70;
    private static final int HOURS_MAX_LENGTH = 20;
    private static final int NAME_MAX_LENGTH = 50;
    private static final int PHONE_MAX_LENGTH = 15;
    private static final int PICTURE_MAX_LENGTH = 100;
    private final UserService userService = new UserService();
    private final RestaurantService restaurantService = new RestaurantService();


    ResourceBundle bundle = LocalizationManager.getBundle();

    public void initialize() {
        TextFieldUtils.addTextLimiter(addressField, STREET_ADDRESS_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(hoursField, HOURS_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(nameField, NAME_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(phoneField, PHONE_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(pictureURL, PICTURE_MAX_LENGTH);
        populateOwnerSelector();
    }

    public void saveRestaurant(ActionEvent actionEvent) {
        String address = addressField.getText();
        String hours = hoursField.getText();
        String name = nameField.getText();
        String phone = phoneField.getText();
        String picture = pictureURL.getText();
        String owner = dialogOwnerSelector.getValue();

        if (address.isEmpty() || hours.isEmpty() || name.isEmpty() || phone.isEmpty() || picture.isEmpty() || owner.isEmpty()) {
            showError(bundle.getString("Fill_all_fields"));
            return;
        }

        // Extract owner ID
        String[] parts = owner.split(" ID: ");
        if (parts.length != 2) {
            showError(bundle.getString("Invalid_owner_selection"));
            return;
        }

        int ownerId = Integer.parseInt(parts[1].trim());

        boolean isSaved = restaurantService.saveRestaurant(new Restaurant(name, phone, picture, address, hours, ownerId));

        if (isSaved) {
            // Success alert
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle(bundle.getString("SuccessTitle"));
            successAlert.setHeaderText(null);
            successAlert.setContentText(bundle.getString("RestaurantSavedSuccessfully"));
            successAlert.showAndWait();
        } else {
            // Failure alert
            Alert failureAlert = new Alert(Alert.AlertType.ERROR);
            failureAlert.setTitle(bundle.getString("ErrorTitle"));
            failureAlert.setHeaderText(null);
            failureAlert.setContentText(bundle.getString("Failed_to_save_restaurant"));
            failureAlert.showAndWait();
        }

        closeDialog();
    }


    public void populateOwnerSelector() {
        try {
            List<User> users = userService.getAllUsers();

            for (User user : users) {
                if (user.getRole().equals("OWNER"))
                    dialogOwnerSelector.getItems().add(user.getUsername() + " ID: " + user.getId());
            }

        } catch (IOException e) {
            showError(bundle.getString("Failed_to_load_user"));
            e.printStackTrace();
        }
    }

    public void closeDialog() {
        Stage stage = (Stage) addressField.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
