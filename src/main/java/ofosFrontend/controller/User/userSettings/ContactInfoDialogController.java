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

import java.util.ResourceBundle;


public class ContactInfoDialogController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField streetAddressField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;
    private ContactInfo contactInfo;
    ResourceBundle bundle = LocalizationManager.getBundle();

    private ContactInfoService contactInfoService = new ContactInfoService();

    private static final int FIRST_NAME_MAX_LENGTH = 20;
    private static final int LAST_NAME_MAX_LENGTH = 20;
    private static final int EMAIL_MAX_LENGTH = 50;
    private static final int PHONE_MAX_LENGTH = 15;
    private static final int ADDRESS_MAX_LENGTH = 70;
    private static final int CITY_MAX_LENGTH = 30;
    private static final int POSTAL_CODE_MAX_LENGTH = 10;

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

    @FXML
    private void handleSave() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String streetAddress = streetAddressField.getText().trim();
        String city = cityField.getText().trim();
        String postalCode = postalCodeField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()
                || streetAddress.isEmpty() || city.isEmpty() || postalCode.isEmpty()) {
            showError(bundle.getString("Fill_all_fields"));
            return;
        }

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setFirstName(firstName);
        contactInfo.setLastName(lastName);
        contactInfo.setEmail(email);
        contactInfo.setPhoneNumber(phoneNumber);
        contactInfo.setAddress(streetAddress);
        contactInfo.setCity(city);
        contactInfo.setPostalCode(postalCode);

        saveContactInfo(contactInfo);
    }

    private void saveContactInfo(ContactInfo contactInfo) {
        Task<Void> task = contactInfoService.saveContactInfo(contactInfo);

        task.setOnSucceeded(event -> {
            Platform.runLater(() -> {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle(bundle.getString("Success"));
                successAlert.setHeaderText(null);
                successAlert.setContentText(bundle.getString("Contact_info_added_successfully"));
                successAlert.showAndWait();
                Stage stage = (Stage) firstNameField.getScene().getWindow();
                stage.close();
            });
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
            Platform.runLater(() -> showError(bundle.getString("Contact_info_save_fail")));
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void handleCancel() {
        // Close the dialog
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString("Contact_Information"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setContactInfo(ContactInfo contactInfo) {
        System.out.println("Set Contact Info in contactinfo dialog controller");

        if (contactInfo == null) {
            return;
        }
        this.contactInfo = contactInfo;
        firstNameField.setText(contactInfo.getFirstName());
        lastNameField.setText(contactInfo.getLastName());
        emailField.setText(contactInfo.getEmail());
        phoneNumberField.setText(contactInfo.getPhoneNumber());
        streetAddressField.setText(contactInfo.getAddress());
        cityField.setText(contactInfo.getCity());
        postalCodeField.setText(contactInfo.getPostalCode());

    }

}
