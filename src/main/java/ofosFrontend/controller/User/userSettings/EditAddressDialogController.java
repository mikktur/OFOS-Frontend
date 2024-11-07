package ofosFrontend.controller.User.userSettings;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ofosFrontend.model.DeliveryAddress;
import ofosFrontend.service.DeliveryAddressService;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.TextFieldUtils;

import java.util.ResourceBundle;

public class EditAddressDialogController {

    @FXML private TextField streetAddressField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;
    @FXML private TextArea instructionsArea;

    private static final int STREET_ADDRESS_MAX_LENGTH = 70;
    private static final int CITY_MAX_LENGTH = 30;
    private static final int POSTAL_CODE_MAX_LENGTH = 10;
    private static final int INSTRUCTIONS_MAX_LENGTH = 50;

    ResourceBundle bundle = LocalizationManager.getBundle();
    private DeliveryAddress address;
    private DeliveryAddressService deliveryAddressService = new DeliveryAddressService();

    @FXML
    public void initialize() {
        TextFieldUtils.addTextLimiter(streetAddressField, STREET_ADDRESS_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(cityField, CITY_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(postalCodeField, POSTAL_CODE_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(instructionsArea, INSTRUCTIONS_MAX_LENGTH);
    }

    public void setAddress(DeliveryAddress address) {
        this.address = address;
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
            showError(bundle.getString("Fill_all_fields"));
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
        Task<Void> task = deliveryAddressService.updateDeliveryAddress(address);

        task.setOnSucceeded(event -> {
            Platform.runLater(() -> {
                Stage stage = (Stage) streetAddressField.getScene().getWindow();
                stage.close();
            });
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
            Platform.runLater(() -> showError(bundle.getString("Update_address_error")));
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void handleCancel() {
        // Close the dialog
        Stage stage = (Stage) streetAddressField.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString("EditAddress"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
