package ofosFrontend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ofosFrontend.AppManager;
import ofosFrontend.controller.User.BasicController;
import ofosFrontend.controller.User.MMenuController;
import ofosFrontend.service.RestaurantService;
import ofosFrontend.service.UserService;
import ofosFrontend.session.SessionManager;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

public class LoginController extends BasicController {
    public Label passwordErrorLabel;
    public Label usernameErrorLabel;
    public Label loginPasswordErrorLabel;
    @FXML
    private Button signUpButton;
    @FXML
    private Button createUserButton;
    @FXML
    private Button goBackButton;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    private final UserService userService = new UserService();
    private final RestaurantService restaurantService = new RestaurantService();
    private final FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/mainUI.fxml"));
    private String role;



    @FXML
    public void initialize() {
        //used  to initialize the session manager
        SessionManager.getInstance();
    }

    @FXML
    public void userLogin(ActionEvent event) {
        new Thread(() -> {
            try {
                Response response = userService.login(username.getText(), password.getText());
                Platform.runLater(() -> handleLoginResponse(response));
            } catch (IOException e) {
                Platform.runLater(() -> {
                    System.out.println("Login failed.");
                    e.printStackTrace();
                    showError("Login error: " + e.getMessage());
                });
            }
        }).start();
    }

    private void handleLoginResponse(Response response)  {
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
                System.out.println("User ID: " + manager.getUserId());

                System.out.println("Token: " + manager.getToken());
                System.out.println("Username: " + manager.getUsername());

                if (manager.getRole().equals("OWNER")) {
                    goToAdmin();
                } else
                    super.goToMain();


                System.out.println("Login successful.");
            } else if (response.code() == 401) { // Unauthorized error
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> errors = objectMapper.readValue(response.body().string(), Map.class);
                updateLoginErrorLabel(errors);
            } else {
                showError("Unexpected response code: " + response.code());
            }
        } catch (IOException e) {
            System.out.println("Failed to handle the response.");
            e.printStackTrace();
            showError("Error processing login response.");
        }
    }

    private void goToAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/adminMainUI.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) AppManager.getInstance().getPrimaryStage();

            Scene adminScene = new Scene(root, 650, 400);

            currentStage.setTitle("OFOS Admin");

            currentStage.setScene(adminScene);

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void goToRegister() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/registerUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) AppManager.getInstance().getPrimaryStage();

        Scene registerScene = new Scene(root, 650, 400);


        currentStage.setTitle("OFOS Register");

        currentStage.setScene(registerScene);

        currentStage.show();
    }

    @FXML
    private void backToLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/loginUI.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) AppManager.getInstance().getPrimaryStage();



        Scene loginScene = new Scene(root, 650, 400);

        currentStage.setTitle("OFOS Login");

        currentStage.setScene(loginScene);

        currentStage.show();
    }

    @FXML
    public void registerUser(ActionEvent event) {
        new Thread(() -> {
            try {
                Response response = userService.register(username.getText(), password.getText());
                Platform.runLater(() -> handleRegisterResponse(response));
            } catch (IOException e) {
                Platform.runLater(() -> {
                    System.out.println("Registration failed.");
                    e.printStackTrace();
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
                System.out.println("User registration successful.");
            } else if (response.code() == 400) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> errors = objectMapper.readValue(response.body().string(), Map.class);
                updateRegistrationErrorLabels(errors);
            } else {
                showError("Unexpected response code: " + response.code());
            }
        } catch (IOException e) {
            System.out.println("Failed to handle the response.");
            e.printStackTrace();
            showError("Error processing registration response.");
        }
    }

    private void updateRegistrationErrorLabels(Map<String, String> errors) {
        String usernameError = errors.get("username");
        usernameErrorLabel.setText(usernameError != null ? usernameError : "");
        usernameErrorLabel.setVisible(usernameError != null);

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
        System.out.println(message);
    }
}