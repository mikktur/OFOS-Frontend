package ofosFrontend.controller.User;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ofosFrontend.model.ContactInfo;
import ofosFrontend.session.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ContactInfoDialogController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField streetAddressField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;

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
        try {
            String url = "http://localhost:8000/api/contactinfo/save";

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(contactInfo);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            responseFuture.thenAccept(response -> {
                if (response.statusCode() == 200) {
                    Platform.runLater(() -> {
                        // Close the dialog
                        Stage stage = (Stage) firstNameField.getScene().getWindow();
                        stage.close();
                    });
                } else {
                    Platform.runLater(() -> showError("Failed to save contact information. Status code: " + response.statusCode()));
                }
            }).exceptionally(ex -> {
                ex.printStackTrace();
                Platform.runLater(() -> showError("An error occurred while saving contact information."));
                return null;
            });

        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred while saving contact information.");
        }
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
