package ofosFrontend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ofosFrontend.AppManager;
import ofosFrontend.service.UserService;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;
import okhttp3.Response;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import java.util.Map;


public class LoginController {
    @FXML
    private Label passwordErrorLabel;
    @FXML
    private Label usernameErrorLabel;
    @FXML
    private Label loginPasswordErrorLabel;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    private final UserService userService = new UserService();
    private final Logger logger = LogManager.getLogger(LoginController.class);


    @FXML
    public void initialize() {
        //used  to initialize the session manager
        SessionManager.getInstance();
    }

    @FXML
    public void userLogin() {
        new Thread(() -> {
            try {
                Response response = userService.login(username.getText(), password.getText());
                Platform.runLater(() -> handleLoginResponse(response));
            } catch (IOException e) {
                Platform.runLater(() -> {
                    logger.log(Level.ERROR,"Login failed.");

                    showError("Login error: " + e.getMessage());
                });
            }
        }).start();
    }

    private void handleLoginResponse(Response response) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            if (response.isSuccessful()) {

                SessionManager manager = SessionManager.getInstance();
                String responseBody = response.body().string();
                Map<String, String> body = mapper.readValue(responseBody, Map.class);
                manager.setToken(body.get("token"));
                manager.setUsername(body.get("username"));
                manager.setRole(body.get("role"));
                Object userIdObj = body.get("userId");

                //vois tehä simppelimmin
                if (userIdObj instanceof String) {

                    manager.setUserId(Integer.parseInt((String) userIdObj));
                } else if (userIdObj instanceof Integer) {

                    manager.setUserId((Integer) userIdObj);
                } else {

                    throw new IllegalArgumentException("Invalid userId type: " + userIdObj);
                }
                logger.info("User ID: {}", manager.getUserId());

                openMainStage();
            } else if (response.code() == 401) { // Unauthorized error
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> errors = objectMapper.readValue(response.body().string(), Map.class);
                updateLoginErrorLabel(errors);
            } else {
                showError("Unexpected response code: " + response.code());
            }
        } catch (IOException e) {
            logger.log(Level.ERROR,"Failed to handle the response.");

            showError("Error processing login response.");
        }

    }



    @FXML
    private void goToRegister() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/registerUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = AppManager.getInstance().getPrimaryStage();

        Scene registerScene = new Scene(root, 650, 400);


        currentStage.setTitle("OFOS Register");

        currentStage.setScene(registerScene);

        currentStage.show();
    }

    @FXML
    private void backToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/loginUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = AppManager.getInstance().getPrimaryStage();


        Scene loginScene = new Scene(root, 650, 400);

        currentStage.setTitle("OFOS Login");

        currentStage.setScene(loginScene);

        currentStage.show();
    }

    @FXML
    public void registerUser() {
        new Thread(() -> {
            try {
                Response response = userService.register(username.getText(), password.getText());
                Platform.runLater(() -> handleRegisterResponse(response));
            } catch (IOException e) {
                Platform.runLater(() -> {
                    logger.log(Level.ERROR,"Registration failed.");
                    showError("Registration error: " + e.getMessage());
                });
            }
        }).start();
    }

    private void handleRegisterResponse(Response response) {
        try {
            if (response.isSuccessful()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/loginUI.fxml"));
                Parent root = loader.load();
                Stage currentStage = AppManager.getInstance().getPrimaryStage();
                Scene registerScene = new Scene(root, 650, 400);
                currentStage.setTitle("OFOS Login");
                currentStage.setScene(registerScene);
                currentStage.show();
                logger.info("User registration successful.");
            } else if (response.code() == 400) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> errors = objectMapper.readValue(response.body().string(), Map.class);
                updateRegistrationErrorLabels(errors);
            } else {
                showError("Unexpected response code: " + response.code());
            }
        } catch (IOException e) {
            logger.log(Level.ERROR,"Failed to handle the response.");
            showError("Error processing registration response.");
        }
    }

    private void updateRegistrationErrorLabels(Map<String, String> errors) {
        // Handles username error text under the username field
        String usernameError = errors.get("username");
        usernameErrorLabel.setText(usernameError != null ? usernameError : "");
        usernameErrorLabel.setVisible(usernameError != null);

        // Sets the password error text under the password field
        String passwordError = errors.get("password");
        passwordErrorLabel.setText(passwordError != null ? passwordError : "");
        passwordErrorLabel.setVisible(passwordError != null);
    }

    private void updateLoginErrorLabel(Map<String, String> errors) {

        String passwordError = errors.get("message");
        loginPasswordErrorLabel.setText(passwordError != null ? passwordError : "");
        loginPasswordErrorLabel.setVisible(passwordError != null);
    }

    private void showError(String message) {
        logger.info(message);
    }


    public void openMainStage() {
        FXMLLoader rootLoader;

        // Select appropriate FXML based on role
        if (SessionManager.getInstance().getRole().equals("OWNER")) {
            rootLoader = new FXMLLoader(getClass().getResource("/ofosFrontend/Owner/ownerRoot.fxml"));
            logger.info("Owner logged in.");
        } else {
            rootLoader = new FXMLLoader(getClass().getResource("/ofosFrontend/root.fxml"));
        }

        try {
            // Use the current ResourceBundle from LocalizationManager
            rootLoader.setResources(LocalizationManager.getBundle());
            BorderPane root = rootLoader.load();

            // Set up the main stage
            Stage mainStage = new Stage();
            Scene menuScene = new Scene(root, 1000, 800);
            mainStage.setTitle("OFOS Menu");
            mainStage.setScene(menuScene);
            AppManager.getInstance().setPrimaryStage(mainStage);
            mainStage.show();

            // Close the login stage
            closeLoginStage();
        } catch (Exception e) {
            logger.error("Failed to open the main stage.", e);
            showError("Failed to open the main stage.");
        }
    }



    private void closeLoginStage() {
        Stage loginStage = (Stage) username.getScene().getWindow();
        loginStage.close();
    }
}