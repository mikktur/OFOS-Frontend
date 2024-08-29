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

public class LoginController {
    @FXML
    private Button signUpButton;
    @FXML
    private Button registerButton;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    public void userLogin(ActionEvent event) throws IOException {
        checkLogin();
    }

    @FXML
    private void checkLogin() throws IOException {
        if (username.getText().toString().equals("johannes") && password.getText().toString().equals("1234")) {
            System.out.println("Login successful! You are the best.");
        } else {
            System.out.println("Login failed, you are not the best.");
        }
    }

    public void initialize() {
        signUpButton.setOnAction(event -> {
            try {
                goToRegister(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
    public void registerUser(ActionEvent event) throws IOException{
        createUser();
    }

    @FXML
    private void createUser() throws IOException {
        System.out.println("User created successfully.");
    }
}