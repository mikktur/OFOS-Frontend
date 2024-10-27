package ofosFrontend.controller.User.userSettings;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ofosFrontend.model.ContactInfo;
import ofosFrontend.service.ContactInfoService;


public class ContactInfoDialogController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField streetAddressField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;

    private ContactInfoService contactInfoService = new ContactInfoService();

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
            showError("Please fill in all required fields.");
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
                Stage stage = (Stage) firstNameField.getScene().getWindow();
                stage.close();
            });
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
            Platform.runLater(() -> showError("An error occurred while saving contact information."));
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
        alert.setTitle("Contact Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
