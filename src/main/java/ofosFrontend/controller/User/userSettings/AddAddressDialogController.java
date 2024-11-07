package ofosFrontend.controller.User.userSettings;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ofosFrontend.model.DeliveryAddress;
import ofosFrontend.service.DeliveryAddressService;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.TextFieldUtils;

import java.util.ResourceBundle;


public class AddAddressDialogController {

    @FXML private TextField streetAddressField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;
    @FXML private TextArea instructionsArea;

    private static final int STREET_ADDRESS_MAX_LENGTH = 70;
    private static final int CITY_MAX_LENGTH = 30;
    private static final int POSTAL_CODE_MAX_LENGTH = 10;
    private static final int INSTRUCTIONS_MAX_LENGTH = 50;

    ResourceBundle bundle = LocalizationManager.getBundle();
    private final DeliveryAddressService deliveryAddressService = new DeliveryAddressService();

    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @FXML
    public void initialize() {
        TextFieldUtils.addTextLimiter(streetAddressField, STREET_ADDRESS_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(cityField, CITY_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(postalCodeField, POSTAL_CODE_MAX_LENGTH);
        TextFieldUtils.addTextLimiter(instructionsArea, INSTRUCTIONS_MAX_LENGTH);
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

        DeliveryAddress newAddress = new DeliveryAddress();
        newAddress.setStreetAddress(streetAddress);
        newAddress.setCity(city);
        newAddress.setPostalCode(postalCode);
        newAddress.setInfo(instructions);

        saveDeliveryAddress(newAddress);
    }


    private void saveDeliveryAddress(DeliveryAddress address) {
        deliveryAddressService.saveDeliveryAddress(address, () -> {
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle(bundle.getString("Success"));
            successAlert.setHeaderText(null);
            successAlert.setContentText(bundle.getString("Address_added_successfully"));
            successAlert.showAndWait();
            Stage stage = (Stage) streetAddressField.getScene().getWindow();
            stage.close();
        }, () -> {
            showError(bundle.getString("Fail_to_save_address"));
        });
    }

    @FXML
    private void handleCancel() {
        // Close the dialog
        Stage stage = (Stage) streetAddressField.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString("Add_New_Address"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public DeliveryAddress getNewAddress() {
        DeliveryAddress newAddress = new DeliveryAddress();
        newAddress.setStreetAddress(streetAddressField.getText());
        newAddress.setPostalCode(postalCodeField.getText());
        newAddress.setCity(cityField.getText());
        newAddress.setInfo(instructionsArea.getText());
        return newAddress;
    }

    public boolean validateInput() {
        return !streetAddressField.getText().isEmpty() &&
                !postalCodeField.getText().isEmpty() &&
                !cityField.getText().isEmpty();
    }
}
