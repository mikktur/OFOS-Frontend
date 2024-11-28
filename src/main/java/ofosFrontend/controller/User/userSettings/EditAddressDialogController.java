package ofosFrontend.controller.User.userSettings;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ofosFrontend.model.DeliveryAddress;
import ofosFrontend.service.DeliveryAddressService;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.TextFieldUtils;
import ofosFrontend.session.Validations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ResourceBundle;

/**
 * Controller class for the Edit Address dialog.
 * Handles the user input and updates the address in the database.
 * The dialog is used to edit an existing delivery address.
 * The user can input the street address, city, postal code, and additional instructions.
 */
public class EditAddressDialogController {

    @FXML
    private TextField streetAddressField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextArea instructionsArea;
    private final Logger logger = LogManager.getLogger(EditAddressDialogController.class);
    private static final int STREET_ADDRESS_MAX_LENGTH = 70;
    private static final int CITY_MAX_LENGTH = 30;
    private static final int POSTAL_CODE_MAX_LENGTH = 10;
    private static final int INSTRUCTIONS_MAX_LENGTH = 50;

    ResourceBundle bundle = LocalizationManager.getBundle();
    private DeliveryAddress address;
    private final DeliveryAddressService deliveryAddressService = new DeliveryAddressService();

    @FXML
    public void initialize() {
        TextFieldUtils.addTextLimiter(streetAddressField, STREET_ADDRESS_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(cityField, CITY_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(postalCodeField, POSTAL_CODE_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(instructionsArea, INSTRUCTIONS_MAX_LENGTH);
    }

    /**
     * Sets the delivery address to be edited and populates the fields with the address data.
     *
     * @param address The delivery address object to be edited.
     */
    public void setAddress(DeliveryAddress address) {
        this.address = address;
        streetAddressField.setText(address.getStreetAddress());
        cityField.setText(address.getCity());
        postalCodeField.setText(address.getPostalCode());
        instructionsArea.setText(address.getInfo());
    }

    /**
     * Handles the save button action.
     * Validates the input fields and updates the address in the database.
     */
    @FXML
    private void handleSave() {
        String validationError = Validations.validateAddressInput(streetAddressField, cityField, postalCodeField, bundle);

        if (validationError != null) {
            Validations.showError(validationError);
            return;
        }

        // Update the address object
        address.setStreetAddress(streetAddressField.getText().trim());
        address.setCity(cityField.getText().trim());
        address.setPostalCode(postalCodeField.getText().trim());
        address.setInfo(instructionsArea.getText().trim());

        updateDeliveryAddress(address);
    }

    /**
     * Updates the delivery address in the database.
     *
     * @param address The DeliveryAddress object containing the updated delivery address.
     */
    private void updateDeliveryAddress(DeliveryAddress address) {
        Task<Void> task = deliveryAddressService.updateDeliveryAddress(address);

        task.setOnSucceeded(event -> Platform.runLater(() -> {
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle(bundle.getString("Success"));
            successAlert.setHeaderText(null);
            successAlert.setContentText(bundle.getString("Address_edited_successfully"));
            successAlert.showAndWait();
            handleCancel();

        }));

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            logger.error("Failed to update delivery address", exception);
            Platform.runLater(() -> Validations.showError(bundle.getString("Update_address_error")));
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Handles the cancel button action to close the dialog.
     */
    @FXML
    private void handleCancel() {
        // Close the dialog
        Stage stage = (Stage) streetAddressField.getScene().getWindow();
        stage.close();
    }



}
