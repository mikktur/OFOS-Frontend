package ofosFrontend.controller.User.userSettings;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ofosFrontend.model.PasswordChangeDTO;
import ofosFrontend.service.UserService;
import ofosFrontend.session.LocalizationManager;

import java.util.ResourceBundle;

public class EditPasswordDialogController {

    @FXML private TextField oldPasswordField;
    @FXML private PasswordField oldPasswordHiddenField;
    @FXML private TextField newPasswordField;
    @FXML private PasswordField newPasswordHiddenField;
    @FXML private CheckBox showPasswordCheckBox;

    private final UserService userService = new UserService();
    private final ResourceBundle bundle = LocalizationManager.getBundle();

    public void initialize() {
        togglePasswordVisibility();  // Initialize with hidden passwords
    }

    @FXML
    private void togglePasswordVisibility() {
        boolean showPassword = showPasswordCheckBox.isSelected();

        // Toggle for old password
        oldPasswordField.setVisible(showPassword);
        oldPasswordHiddenField.setVisible(!showPassword);
        oldPasswordField.setText(oldPasswordHiddenField.getText());
        oldPasswordHiddenField.setText(oldPasswordField.getText());

        // Toggle for new password
        newPasswordField.setVisible(showPassword);
        newPasswordHiddenField.setVisible(!showPassword);
        newPasswordField.setText(newPasswordHiddenField.getText());
        newPasswordHiddenField.setText(newPasswordField.getText());
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) oldPasswordField.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleSave(ActionEvent actionEvent) {
        String oldPassword = showPasswordCheckBox.isSelected() ? oldPasswordField.getText() : oldPasswordHiddenField.getText();
        String newPassword = showPasswordCheckBox.isSelected() ? newPasswordField.getText() : newPasswordHiddenField.getText();
        PasswordChangeDTO passwordDTO = new PasswordChangeDTO(oldPassword, newPassword);

        Task<Void> task = userService.updatePassword(passwordDTO);

        task.setOnSucceeded(event -> {
            Platform.runLater(() -> {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle(bundle.getString("ChangePassword"));
                successAlert.setHeaderText(null);
                successAlert.setContentText(bundle.getString("Password_change_success"));
                successAlert.showAndWait();
                Stage stage = (Stage) oldPasswordField.getScene().getWindow();
                stage.close();
            });
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            exception.printStackTrace();

            Platform.runLater(() -> {
                String errorMessage = exception.getMessage();
                if (errorMessage.contains("Unauthorized request")) {
                    showError(bundle.getString("Password_requirements_not_met"));
                } else if (errorMessage.contains("Failed to update password")) {
                    showError(bundle.getString("Update_password_error"));
                } else {
                    showError(bundle.getString("General_update_password_error"));
                }
            });
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString("ChangePassword"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
