package ofosFrontend.controller.User.userSettings;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ofosFrontend.model.ContactInfo;
import ofosFrontend.service.ContactInfoService;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.TextFieldUtils;
import ofosFrontend.session.Validations;

import java.util.ResourceBundle;


public class ContactInfoDialogController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField streetAddressField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;

    ResourceBundle bundle = LocalizationManager.getBundle();

    private final ContactInfoService contactInfoService = new ContactInfoService();

    private static final int FIRST_NAME_MAX_LENGTH = 20;
    private static final int LAST_NAME_MAX_LENGTH = 20;
    private static final int EMAIL_MAX_LENGTH = 50;
    private static final int PHONE_MAX_LENGTH = 15;
    private static final int ADDRESS_MAX_LENGTH = 70;
    private static final int CITY_MAX_LENGTH = 30;
    private static final int POSTAL_CODE_MAX_LENGTH = 5;

    @FXML
    public void initialize() {
        TextFieldUtils.addTextLimiter(firstNameField, FIRST_NAME_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(lastNameField, LAST_NAME_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(emailField, EMAIL_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(phoneNumberField, PHONE_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(streetAddressField, ADDRESS_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(cityField, CITY_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(postalCodeField, POSTAL_CODE_MAX_LENGTH);
    }

    /**
     * Handles the save button action.
     * Validates the input fields and saves the contact info to the database.
     */

    @FXML
    private void handleSave() {
        String validationError = Validations.validateContactInfo(
                firstNameField, lastNameField, emailField, phoneNumberField,
                streetAddressField, cityField, postalCodeField, bundle
        );

        if (validationError != null) {
            Validations.showError(validationError);
            return;
        }

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setFirstName(firstNameField.getText().trim());
        contactInfo.setLastName(lastNameField.getText().trim());
        contactInfo.setEmail(emailField.getText().trim());
        contactInfo.setPhoneNumber(phoneNumberField.getText().trim());
        contactInfo.setAddress(streetAddressField.getText().trim());
        contactInfo.setCity(cityField.getText().trim());
        contactInfo.setPostalCode(postalCodeField.getText().trim());

        saveContactInfo(contactInfo);
    }


    /**
     * Saves the contact info to the database.
     * Shows a success message if the operation is successful.
     * Shows an error message if the operation fails.
     *
     * @param contactInfo The contact info to save.
     */

    private void saveContactInfo(ContactInfo contactInfo) {
        Task<Void> task = contactInfoService.saveContactInfo(contactInfo);

        task.setOnSucceeded(event -> Platform.runLater(() -> {
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle(bundle.getString("Success"));
            successAlert.setHeaderText(null);
            successAlert.setContentText(bundle.getString("Contact_info_added_successfully"));
            successAlert.showAndWait();
            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.close();
        }));

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
            Platform.runLater(() -> Validations.showError(bundle.getString("Contact_info_save_fail")));
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Handles the cancel button action.
     * Closes the dialog window.
     */

    @FXML
    private void handleCancel() {
        // Close the dialog
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the contact info to display in the dialog.
     *
     * @param contactInfo The contact info to display.
     */
    public void setContactInfo(ContactInfo contactInfo) {

        if (contactInfo == null) return;

        firstNameField.setText(contactInfo.getFirstName());
        lastNameField.setText(contactInfo.getLastName());
        emailField.setText(contactInfo.getEmail());
        phoneNumberField.setText(contactInfo.getPhoneNumber());
        streetAddressField.setText(contactInfo.getAddress());
        cityField.setText(contactInfo.getCity());
        postalCodeField.setText(contactInfo.getPostalCode());
    }
}
