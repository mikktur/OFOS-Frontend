package ofosFrontend.controller.User.userSettings;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ofosFrontend.model.PasswordChangeDTO;
import ofosFrontend.service.UserService;
import ofosFrontend.session.LocalizationManager;

import java.util.ResourceBundle;

public class EditPasswordDialogController {
    public TextField oldPasswordField;
    public TextField newPasswordField;

    private final UserService userService = new UserService();
    ResourceBundle bundle = LocalizationManager.getBundle();

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) oldPasswordField.getScene().getWindow();
        stage.close();
    }

    public void handleSave(ActionEvent actionEvent) {
        PasswordChangeDTO passwordDTO = new PasswordChangeDTO(oldPasswordField.getText(), newPasswordField.getText());

        Task<Void> task = userService.updatePassword(passwordDTO);

        task.setOnSucceeded(event -> {
            // Close the dialog on success
            Platform.runLater(() -> {
                Stage stage = (Stage) oldPasswordField.getScene().getWindow();
                stage.close();
            });
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
            Platform.runLater(() -> showError(bundle.getString("Update_password_error")));
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
