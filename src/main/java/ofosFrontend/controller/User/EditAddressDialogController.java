package ofosFrontend.controller.User;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ofosFrontend.model.DeliveryAddress;
import ofosFrontend.session.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class EditAddressDialogController {

    @FXML private TextField streetAddressField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;
    @FXML private TextArea instructionsArea;

    private DeliveryAddress address;

    public void setAddress(DeliveryAddress address) {
        this.address = address;
        // Pre-fill the fields with existing data
        streetAddressField.setText(address.getStreetAddress());
        cityField.setText(address.getCity());
        postalCodeField.setText(address.getPostalCode());
        instructionsArea.setText(address.getInfo());
    }

    @FXML
    private void handleSave() {
        String streetAddress = streetAddressField.getText();
        String city = cityField.getText();
        String postalCode = postalCodeField.getText();
        String instructions = instructionsArea.getText();

        if (streetAddress.isEmpty() || city.isEmpty() || postalCode.isEmpty()) {
            showError("Please fill in all required fields.");
            return;
        }

        // Update the address object
        address.setStreetAddress(streetAddress);
        address.setCity(city);
        address.setPostalCode(postalCode);
        address.setInfo(instructions);

        updateDeliveryAddress(address);
    }

    private void updateDeliveryAddress(DeliveryAddress address) {
        try {
            String url = "http://localhost:8000/api/deliveryaddress/update";

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(address);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            responseFuture.thenAccept(response -> {
                if (response.statusCode() == 200) {
                    Platform.runLater(() -> {
                        // Close the dialog
                        Stage stage = (Stage) streetAddressField.getScene().getWindow();
                        stage.close();
                    });
                } else {
                    Platform.runLater(() -> showError("Failed to update address. Status code: " + response.statusCode()));
                }
            }).exceptionally(ex -> {
                ex.printStackTrace();
                Platform.runLater(() -> showError("An error occurred while updating the address."));
                return null;
            });

        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred while updating the address.");
        }
    }

    @FXML
    private void handleCancel() {
        // Close the dialog
        Stage stage = (Stage) streetAddressField.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Edit Address");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
