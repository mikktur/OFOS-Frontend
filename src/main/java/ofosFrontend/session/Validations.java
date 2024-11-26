package ofosFrontend.session;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Validations {

    /**
     * Validates the input fields for an address form.
     * @param streetAddressField TextField for the street address.
     * @param cityField TextField for the city.
     * @param postalCodeField TextField for the postal code.
     * @param bundle ResourceBundle for localization.
     * @return An error message if the input is invalid, or null if the input is valid.
     */
    public static String validateAddressInput(TextField streetAddressField, TextField cityField, TextField postalCodeField, ResourceBundle bundle) {

        if (streetAddressField.getText().isEmpty()) {
            streetAddressField.setStyle("-fx-border-color: red;");
            return bundle.getString("Street_address_required");
        } else {
            streetAddressField.setStyle(null);
        }

        if (cityField.getText().isEmpty()) {
            cityField.setStyle("-fx-border-color: red;");
            return bundle.getString("City_required");
        } else {
            cityField.setStyle(null);
        }

        String postalCode = postalCodeField.getText();
        if (postalCode.isEmpty()) {
            postalCodeField.setStyle("-fx-border-color: red;");
            return bundle.getString("Postal_code_required");
        } else if (!postalCode.matches("\\d{5}")) {
            postalCodeField.setStyle("-fx-border-color: red;");
            return bundle.getString("Postal_code_invalid");
        } else {
            postalCodeField.setStyle(null);
        }

        return null; // Input is valid
    }

    /**
     * Validates the input fields for a contact info form.
     *
     * @param firstNameField TextField for the first name.
     * @param lastNameField TextField for the last name.
     * @param emailField TextField for the email.
     * @param phoneNumberField TextField for the phone number.
     * @param streetAddressField TextField for the street address.
     * @param cityField TextField for the city.
     * @param postalCodeField TextField for the postal code.
     * @param bundle ResourceBundle for localization.
     *
     * @return An error message if the input is invalid, or null if the input is valid.
     */
    public static String validateContactInfo(TextField firstNameField, TextField lastNameField,
                                             TextField emailField, TextField phoneNumberField,
                                             TextField streetAddressField, TextField cityField,
                                             TextField postalCodeField, ResourceBundle bundle) {
        if (firstNameField.getText().trim().isEmpty()) {
            return bundle.getString("First_name_required");
        }
        if (lastNameField.getText().trim().isEmpty()) {
            return bundle.getString("Last_name_required");
        }

        // Email validation
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            return bundle.getString("Email_required");
        }
        Pattern emailPattern = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
        if (!emailPattern.matcher(email).matches()) {
            return bundle.getString("Invalid_email_format");
        }

        // Phone Number Validation
        String phoneNumber = phoneNumberField.getText().trim();
        if (phoneNumber.isEmpty()) {
            return bundle.getString("Phone_number_required");
        }
        // Ensure phone number contains only digits
        if (!phoneNumber.matches("\\d+")) {
            return bundle.getString("Invalid_phone_number");
        }

        if (streetAddressField.getText().trim().isEmpty()) {
            return bundle.getString("Street_address_required");
        }
        if (cityField.getText().trim().isEmpty()) {
            return bundle.getString("City_required");
        }
        // Postal Code Validation
        String postalCode = postalCodeField.getText().trim();
        if (postalCode.isEmpty()) {
            return bundle.getString("Postal_code_required");
        }
        // Ensure postal code is exactly 5 digits
        if (!postalCode.matches("\\d{5}")) {
            return bundle.getString("Invalid_postal_code");
        }
        return null; // All validations passed
    }

    /**
     * Validates the old and new passwords for the Edit Password dialog.
     * @param oldPassword The old password input.
     * @param newPassword The new password input.
     * @param bundle ResourceBundle for localized error messages.
     * @return An error message if validation fails, or null if validation passes.
     */
    public static String validatePasswordInput(String oldPassword, String newPassword, ResourceBundle bundle) {
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            return bundle.getString("Old_password_required");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return bundle.getString("New_password_required");
        }
        if (newPassword.length() < 6 || newPassword.length() > 20) {
            return bundle.getString("New_password_length_range");
        }
        if (!newPassword.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return bundle.getString("New_password_special_character_required");
        }
        if (oldPassword.equals(newPassword)) {
            return bundle.getString("Passwords_must_differ");
        }
        return null; // Validation passed
    }

    /**
     * Validates the input fields for a restaurant form.
     * @param address The address input.
     * @param hours The business hours input.
     * @param name The restaurant name input.
     * @param phone The phone number input.
     * @param picture The picture URL input.
     * @param owner The owner input.
     * @return An error message if validation fails, or null if validation passes.
     */
    public static String validateRestaurantInput(String address, String hours, String name, String phone, String picture, String owner) {
        ResourceBundle bundle = LocalizationManager.getBundle();
        if (address == null || address.trim().isEmpty()) {
            return bundle.getString("Address_required");
        }
        if (hours == null || hours.trim().isEmpty()) {
            return bundle.getString("Hours_required");
        }
        if (name == null || name.trim().isEmpty()) {
            return bundle.getString("Name_required");
        }
        if (phone == null || phone.trim().isEmpty()) {
            return bundle.getString("Phone_required");
        }
        if (!phone.matches("\\d+")) { // Ensure phone contains only numbers
            return bundle.getString("Phone_invalid");
        }
        if (picture == null || picture.trim().isEmpty()) {
            return bundle.getString("Picture_required");
        }
        if (picture.length() > 100) { // Check max length for picture URL
            return bundle.getString("Picture_max_length");
        }
        if (owner == null || owner.trim().isEmpty()) {
            return bundle.getString("Owner_required");
        }
        return null; // Validation passed
    }




    /**
     * Shows an error message dialog.
     *
     * @param message The error message to display.
     */
    public static void showError(String message) {
        ResourceBundle bundle = LocalizationManager.getBundle();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString("Error"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showSuccessAlert(String message) {
        ResourceBundle bundle = LocalizationManager.getBundle();
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle(bundle.getString("SuccessTitle"));
        successAlert.setHeaderText(null);
        successAlert.setContentText(message);
        successAlert.showAndWait();
    }
}
