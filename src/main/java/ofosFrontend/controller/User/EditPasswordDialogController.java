package ofosFrontend.controller.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ofosFrontend.model.PasswordChangeDTO;
import ofosFrontend.session.SessionManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class EditPasswordDialogController {
    public TextField oldPasswordField;
    public TextField newPasswordField;
    String token;

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) oldPasswordField.getScene().getWindow();
        stage.close();
    }

    public void handleSave(ActionEvent actionEvent) {
        // PUT method to update the password
        try {
            PasswordChangeDTO passwordDTO = new PasswordChangeDTO(oldPasswordField.getText(),newPasswordField.getText());

            String url = "http://localhost:8000/api/user/updatePassword";

            token = SessionManager.getInstance().getToken();

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(passwordDTO);
            System.out.println("requestbody"+ requestBody);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            responseFuture.thenAccept(response -> {
                if (response.statusCode() == 200) {
                    Platform.runLater(() -> {
                        // Close the dialog
                        Stage stage = (Stage) oldPasswordField.getScene().getWindow();
                        stage.close();
                    });
                } else {
                    Platform.runLater(() -> showError("Failed to update the password. Status code: " + response.statusCode()));
                }
            }).exceptionally(ex -> {
                ex.printStackTrace();
                Platform.runLater(() -> showError("An error occurred while updating the password."));
                return null;
            });
          } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred while updating the password.");
        }
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Change password");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
