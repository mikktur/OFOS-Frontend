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
import ofosFrontend.model.LoginResponse;
import ofosFrontend.service.UserService;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;
import okhttp3.Response;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static ofosFrontend.session.Validations.showError;

/**
 * Controller for the login view
 * This class is responsible for handling user login and registration
 * and opening the main view
 */
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

    /**
     * Initialize the login controller
     * Get the session manager instance
     */
    @FXML
    public void initialize() {
        //used  to initialize the session manager
        SessionManager.getInstance();
    }

    /**
     * Handle user login
     *
     * @param event the event that triggered the login
     */
    @FXML
    public void userLogin() {
        new Thread(() -> {
            try {
                LoginResponse loginResponse = userService.login(username.getText(), password.getText());
                Platform.runLater(() -> handleLoginResponse(loginResponse));
            } catch (IOException e) {
                Platform.runLater(() -> {
                    logger.log(Level.ERROR, "Login failed.");
                    showError("Login error: " + e.getMessage());
                });
            }
        }).start();
    }

    /**
     * Handle the login response
     *
     * @param loginResponse the response from the login request
     *                 if the response is successful, set the session manager token, username, role and user id
     *                 and open the main stage
     */
    private void handleLoginResponse(LoginResponse loginResponse) {

        ObjectMapper mapper = new ObjectMapper();

        if (loginResponse.getStatusCode() == 200) {
            SessionManager manager = SessionManager.getInstance();
            Map<String, String> body = loginResponse.getBody();
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
        } else if (loginResponse.getStatusCode() == 400) {
            Map<String, String> errors = loginResponse.getBody();
            updateLoginErrorLabel(errors);
        } else {
            showError("Unexpected response code: " + loginResponse.getStatusCode());
        }


        openMainStage();

    }

    /**
     * Go to the register view
     *
     * @throws IOException if the register view cannot be loaded
     */
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

    /**
     * Go back to the login view
     *
     * @param event the event that triggered the go back
     * @throws IOException if the login view cannot be loaded
     */
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

    /**
     * Register a new user
     */
    @FXML
    public void registerUser() {
        new Thread(() -> {
            try {
                Response response = userService.register(username.getText(), password.getText());
                Platform.runLater(() -> handleRegisterResponse(response));
            } catch (IOException e) {
                Platform.runLater(() -> {
                    logger.log(Level.ERROR, "Registration failed.");
                    showError("Registration error: " + e.getMessage());
                });
            }
        }).start();
    }

    /**
     * Handle the registration response
     *
     * @param response the response from the registration request
     *                 if the response is successful, open the login view
     *                 if the response is a bad request, display the error messages
     */
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
            logger.log(Level.ERROR, "Failed to handle the response.");
            showError("Error processing registration response.");
        }
    }

    /**
     * Update the registration error labels
     *
     * @param errors the errors to display
     */
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

    /**
     * Update the login error label
     *
     * @param errors the errors to display
     */
    private void updateLoginErrorLabel(Map<String, String> errors) {

        String passwordError = errors.get("message");
        loginPasswordErrorLabel.setText(passwordError != null ? passwordError : "");
        loginPasswordErrorLabel.setVisible(passwordError != null);
    }


    /**
     * Open the main stage
     * Load the appropriate FXML based on the user role
     */
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

            rootLoader.setResources(LocalizationManager.getBundle());
            logger.debug("Loading main stage. : {}", LocalizationManager.getBundle().getLocale());
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

    /**
     * Close the login stage
     */
    private void closeLoginStage() {

        Stage loginStage = (Stage) username.getScene().getWindow();
        loginStage.close();
    }
}