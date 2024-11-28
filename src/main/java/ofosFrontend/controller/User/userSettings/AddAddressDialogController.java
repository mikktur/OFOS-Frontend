package ofosFrontend.controller.User.userSettings;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ofosFrontend.model.DeliveryAddress;
import ofosFrontend.service.DeliveryAddressService;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.TextFieldUtils;
import ofosFrontend.session.Validations;

import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Controller class for the Add Address dialog.
 * Handles the user input and saves the new address to the database.
 * The dialog is used to add a new delivery address to the user's account.
 * The user can input the street address, city, postal code, and additional instructions.
 */
public class AddAddressDialogController {

    @FXML private TextField streetAddressField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;
    @FXML private TextArea instructionsArea;
    protected static final Logger logger = LogManager.getLogger();
    private static final int STREET_ADDRESS_MAX_LENGTH = 70;
    private static final int CITY_MAX_LENGTH = 30;
    private static final int POSTAL_CODE_MAX_LENGTH = 5;
    private static final int INSTRUCTIONS_MAX_LENGTH = 50;
    private int userId;
    ResourceBundle bundle = LocalizationManager.getBundle();
    private final DeliveryAddressService deliveryAddressService;

    public AddAddressDialogController(DeliveryAddressService deliveryAddressService) {
        this.deliveryAddressService = deliveryAddressService;
    }

    /**
     * Default constructor.
     * Initializes the delivery address service.
     */
    public AddAddressDialogController() {
        this.deliveryAddressService = new DeliveryAddressService();
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Initializes the dialog.
     * Adds text limiters to the input fields.
     */
    @FXML
    public void initialize() {
        TextFieldUtils.addTextLimiter(streetAddressField, STREET_ADDRESS_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(cityField, CITY_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(postalCodeField, POSTAL_CODE_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(instructionsArea, INSTRUCTIONS_MAX_LENGTH);
    }

    /**
     * Handles the save button click event.
     * Validates the input fields and saves the new address to the database.
     */
    @FXML
    private void handleSave() {
        String validationError = validateInput();
        if (validationError != null) {
            Validations.showError(validationError);
            return;
        }

        DeliveryAddress newAddress = getNewAddress();
        logger.info("New Address: {}", newAddress);
        saveDeliveryAddress(newAddress);
    }


    /**
     * Saves the new delivery address to the database.
     * @param address The object containing the new delivery address to save.
     * The address is saved to the database using the delivery address service.
     */
    private void saveDeliveryAddress(DeliveryAddress address) {
        deliveryAddressService.saveDeliveryAddress(address, () -> {
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle(bundle.getString("Success"));
            successAlert.setHeaderText(null);
            successAlert.setContentText(bundle.getString("Address_added_successfully"));
            successAlert.showAndWait();
            Stage stage = (Stage) streetAddressField.getScene().getWindow();
            stage.close();
        }, () -> Validations.showError(bundle.getString("Fail_to_save_address")));
    }

    /**
     * Handles the cancel button click event.
     * Closes the dialog window.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) streetAddressField.getScene().getWindow();
        stage.close();
    }


    /**
     * Creates a new DeliveryAddress object from the user input.
     * @return The new DeliveryAddress object containing the user input.
     */
    public DeliveryAddress getNewAddress() {
        DeliveryAddress newAddress = new DeliveryAddress();

        String streetAddress = streetAddressField.getText();
        String city = cityField.getText();
        String postalCode = postalCodeField.getText();
        String instructions = instructionsArea.getText();

        newAddress.setStreetAddress(streetAddress != null ? streetAddress : "");
        newAddress.setCity(city != null ? city : "");
        newAddress.setPostalCode(postalCode != null ? postalCode : "");
        newAddress.setInfo(instructions);

        return newAddress;
    }

    /**
     * Validates the user input.
     * @return An error message if the input is invalid, or null if the input is valid.
     */
    public String validateInput() {
        return Validations.validateAddressInput(streetAddressField, cityField, postalCodeField, bundle);
    }


}
