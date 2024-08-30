package ofosFrontend.controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginController {

    @FXML
    private Button signUpButton;
    @FXML
    private Button goBackButton;
    @FXML
    private Button registerButton;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private static final String REGISTER_URL = "http://localhost:8080/api/register";

    @FXML
    public void userLogin(ActionEvent event) throws IOException {
        checkLogin();
    }

    @FXML
    private void checkLogin() throws IOException {
        if (username.getText().toString().equals("johannes") && password.getText().toString().equals("1234")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/menuUI.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) signUpButton.getScene().getWindow();

            Scene registerScene = new Scene(root, 600, 400);

            currentStage.setTitle("OFOS Menu");

            currentStage.setScene(registerScene);

            currentStage.show();

            System.out.println("Login successful! You are the best.");
        } else {
            System.out.println("Login failed, you are not the best.");
        }
    }

    @FXML
    private void goToRegister(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/registerUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) signUpButton.getScene().getWindow();

        Scene registerScene = new Scene(root, 600, 400);

        currentStage.setTitle("OFOS Register");

        currentStage.setScene(registerScene);

        currentStage.show();
    }

    @FXML
    private void backToLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/loginUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) goBackButton.getScene().getWindow();

        Scene registerScene = new Scene(root, 600, 400);

        currentStage.setTitle("OFOS Login");

        currentStage.setScene(registerScene);

        currentStage.show();
    }

    @FXML
    public void registerUser(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Validation Error", "Please enter both username and password.");
            return;
        }

        try {
            String jsonBody = createJsonBody(username, password);
            sendPostRequest(jsonBody);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while trying to register.");
        }
    }

    private String createJsonBody(String username, String password) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User(username, password);
        return mapper.writeValueAsString(user);
    }

    private void sendPostRequest(String jsonBody) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(REGISTER_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            showAlert("Success", "Registration successful!");
            backToLogin(null);
        } else {
            showAlert("Error", "Registration failed. Status code: " + response.statusCode());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    static class User {
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}