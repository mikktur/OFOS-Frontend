package ofosFrontend.controller.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.User;
import ofosFrontend.service.RestaurantService;
import ofosFrontend.service.UserService;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.TextFieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import static ofosFrontend.session.Validations.*;

/**
 * Controller class for the Add Restaurant dialog.
 * Handles the user input and saves the restaurant to the database.
 * The dialog is used to add a new restaurant to the system.
 */
public class AddRestaurantDialogController {

    @FXML
    private TextField addressField;
    @FXML
    private TextField hoursField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField pictureURL;
    @FXML
    private ComboBox<String> dialogOwnerSelector;

    private static final int STREET_ADDRESS_MAX_LENGTH = 70;
    private static final int HOURS_MAX_LENGTH = 20;
    private static final int NAME_MAX_LENGTH = 50;
    private static final int PHONE_MAX_LENGTH = 15;
    private static final int PICTURE_MAX_LENGTH = 100;
    private final Logger logger = LogManager.getLogger(AddRestaurantDialogController.class);
    private final UserService userService = new UserService();
    private final RestaurantService restaurantService = new RestaurantService();
    private final ResourceBundle bundle = LocalizationManager.getBundle();

    /**
     * Initializes the dialog.
     * Adds text limiters to the input fields and populates the owner selector.
     */
    public void initialize() {
        TextFieldUtils.addTextLimiter(addressField, STREET_ADDRESS_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(hoursField, HOURS_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(nameField, NAME_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(phoneField, PHONE_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(pictureURL, PICTURE_MAX_LENGTH);
        populateOwnerSelector();
    }

    /**
     * Handles the save button action.
     * @param actionEvent The event that triggered the action.
     * Validates the input fields and saves the restaurant to the database.
     */
    @FXML
    public void saveRestaurant(ActionEvent actionEvent) {
        String address = addressField.getText().trim();
        String hours = hoursField.getText().trim();
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String picture = pictureURL.getText().trim();
        String owner = dialogOwnerSelector.getValue();

        // Validate input
        String validationError = validateRestaurantInput(address, hours, name, phone, picture, owner);
        if (validationError != null) {
            showError(validationError);
            return;
        }

        // Extract owner ID
        String[] parts = owner.split(" ID: ");
        if (parts.length != 2) {
            showError(bundle.getString("Invalid_owner_selection"));
            return;
        }

        int ownerId;
        try {
            ownerId = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            showError(bundle.getString("Invalid_owner_selection"));
            return;
        }

        Restaurant restaurant = new Restaurant(name, phone, picture, address, hours, ownerId);
        boolean isSaved = restaurantService.saveRestaurant(restaurant);

        if (isSaved) {
            showSuccessAlert(bundle.getString("RestaurantSavedSuccessfully"));
        } else {
            showError(bundle.getString("Failed_to_save_restaurant"));
        }

        closeDialog();
    }

    /**
     * Populates the owner selector with all users that have the role "OWNER".
     */
    private void populateOwnerSelector() {
        try {
            List<User> users = userService.getAllUsers();
            users.stream()
                    .filter(user -> "OWNER".equals(user.getRole()))
                    .forEach(user -> dialogOwnerSelector.getItems().add(user.getUsername() + " ID: " + user.getUserId()));
        } catch (IOException e) {
            showError(bundle.getString("Failed_to_load_user"));
            logger.error("Failed to load users: {}", e.getMessage());
        }
    }

    /**
     * Closes the dialog.
     */
    @FXML
    private void closeDialog() {
        Stage stage = (Stage) addressField.getScene().getWindow();
        stage.close();
    }


}
