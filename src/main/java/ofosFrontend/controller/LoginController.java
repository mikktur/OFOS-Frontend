package ofosFrontend.controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button button;
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

    @FXML
    private void goToRegister(ActionEvent event) throws IOException {
        System.out.println("Going to register page.");
    }
}