package ofosFrontend.controller.User.userSettings;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import ofosFrontend.controller.User.BasicController;
import ofosFrontend.controller.User.DropDownMenuController;
import ofosFrontend.model.ContactInfo;
import ofosFrontend.model.DeliveryAddress;
import ofosFrontend.service.DeliveryAddressService;
import ofosFrontend.service.UserService;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

import static ofosFrontend.session.GenericHelper.executeTask;
import static ofosFrontend.session.Validations.showError;

/**
 * Controller class for the User Settings view.
 * Handles the user's contact information, delivery addresses, and password.
 * The user can view and edit their contact information, add, edit, and delete delivery addresses,
 * and change their password or delete their account.
 */
public class UserSettingsController extends BasicController {

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneNumberLabel;
    @FXML private Label cityLabel;
    @FXML private Label postalCodeLabel;
    @FXML private Label addressLabel;
    @FXML private VBox deliveryAddressContainer;
    @FXML private Button changePassword;
    private static final Logger logger = LogManager.getLogger(UserSettingsController.class);
    ResourceBundle bundle = LocalizationManager.getBundle();
    private int userId;
    private List<DeliveryAddress> deliveryAddressesList = new ArrayList<>();
    private final DeliveryAddressService deliveryAddressService = new DeliveryAddressService();
    private final UserService userService = new UserService();
    private ContactInfo currentContactInfo;

    /**
     * Initializes the view.
     * Fetches the user's contact information and delivery addresses.
     * Sets up the change password button with a rotation animation :D
     */
    public void initialize() {

        this.userId = SessionManager.getInstance().getUserId();

        fetchUserData();
        fetchDeliveryAddresses();
        RotateTransition rotate = new RotateTransition(Duration.millis(400), changePassword);
        rotate.setByAngle(360);
        changePassword.setOnMouseEntered(event -> {
            rotate.setCycleCount(Animation.INDEFINITE);
            rotate.setRate(1);
            rotate.play();
        });

        changePassword.setOnMouseExited(event -> Platform.runLater(() -> {
            rotate.stop();
            changePassword.setRotate(0);
        }));
    }

    /**
     * Fetches the user's contact information from the database.
     * If the user has no contact information, prompts the user to add it.
     * If the user has contact information, updates the UI with the information.
     */
    private void fetchUserData() {
        userId = SessionManager.getInstance().getUserId();

        executeTask(
                userService.fetchUserData(userId),
                contactInfo -> Platform.runLater(() -> updateContactInfoUI(contactInfo)),
                () -> showError(bundle.getString("An_error_occurred_while_fetching_contact_information"))
        );
    }

    /**
     * Updates the UI with the user's contact information.
     * @param contactInfo The ContactInfo object to display in the UI.
     * If the contactInfo is null, prompts the user to add contact information.
     * If the contactInfo is not null, updates the UI with the information.
     */
    private void updateContactInfoUI(ContactInfo contactInfo) {
        if (contactInfo == null) {
            promptForContactInfo();
            return;
        }

        currentContactInfo = contactInfo;
        nameLabel.setText(contactInfo.getFirstName() + " " + contactInfo.getLastName());
        emailLabel.setText(contactInfo.getEmail());
        phoneNumberLabel.setText(contactInfo.getPhoneNumber());
        addressLabel.setText(contactInfo.getAddress());
        cityLabel.setText(contactInfo.getCity());
        postalCodeLabel.setText(contactInfo.getPostalCode());
    }

    /**
     * Prompts the user to add contact information.
     * If the user chooses to add contact information, opens the contact info dialog.
     * If the user chooses not to add contact information, updates the UI accordingly.
     * If the user has contact information, updates the UI with the information.
     */
    private void promptForContactInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("setTitleText"));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString("setContentText"));

        ButtonType yesButton = new ButtonType(bundle.getString("YesButton"));
        ButtonType noButton = new ButtonType(bundle.getString("NoButton"), ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            ContactInfo contactInfo = null;
            // Open the dialog to add contact information
            openContactInfoDialog(contactInfo);
        } else {
            // User chose not to add contact information now
            // Update the UI accordingly
            Platform.runLater(() -> {
                nameLabel.setText(bundle.getString("No_name_available"));
                emailLabel.setText(bundle.getString("No_email_available"));
                phoneNumberLabel.setText(bundle.getString("No_phone_number_available"));
                cityLabel.setText(bundle.getString("No_city_available"));
                postalCodeLabel.setText(bundle.getString("No_postal_code_available"));
                addressLabel.setText(bundle.getString("No_address_available"));
            });
        }
    }

    /**
     * Opens the contact information dialog.
     * @param contactInfo The contact information object to display in the dialog.
     * If the contactInfo is null, the dialog will be empty for the user to fill in.
     * If the contactInfo is not null, the dialog will display the existing information.
     */
    private void openContactInfoDialog(ContactInfo contactInfo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/contactInfoDialog.fxml"));
            loader.setResources(LocalizationManager.getBundle());
            Parent root = loader.load();

            ContactInfoDialogController dialogController = loader.getController();
            dialogController.setContactInfo(contactInfo);

            Stage stage = new Stage();
            stage.setTitle(bundle.getString("Modify_Contact_Information"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // After the dialog is closed, fetch the contact information again
            fetchUserData();

        } catch (IOException e) {
            logger.error("Error opening contact info dialog", e);

            showError(bundle.getString("Contact_info_error"));
        }
    }


    /**
     * Fetches the user's delivery addresses from the database.
     * Updates the UI with the delivery addresses.
     */
    private void fetchDeliveryAddresses() {
        executeTask(
                deliveryAddressService.fetchDeliveryAddresses(userId),
                addresses -> {
                    deliveryAddressesList = addresses;
                    updateDeliveryAddressesUI();
                },
                () -> showError(bundle.getString("Delivery_address_fetch_error"))
        );
    }


    /**
     * Updates the UI with the user's delivery addresses.
     * If the user has no delivery addresses, displays a placeholder message.
     * If the user has delivery addresses, displays each address in a separate node.
     */
    private void updateDeliveryAddressesUI() {
        Platform.runLater(() -> {
            deliveryAddressContainer.getChildren().clear();

            if (deliveryAddressesList.isEmpty()) {
                addPlaceholderToContainer();
            } else {
                deliveryAddressesList.forEach(address -> deliveryAddressContainer.getChildren().add(createAddressNode(address)));
            }
        });
    }

    /**
     * Adds a placeholder message to the delivery address container.
     */
    private void addPlaceholderToContainer() {
        Label placeholderLabel = new Label(bundle.getString("No_saved_delivery_addresses"));
        placeholderLabel.setStyle("-fx-font-style: italic; -fx-text-fill: gray;");
        placeholderLabel.setPadding(new Insets(10));
        deliveryAddressContainer.getChildren().add(placeholderLabel);
    }

    /**
     * Creates a new Node for displaying a delivery address.
     * The Node contains the address information, buttons for editing and deleting the address,
     * and a button to set the address as the default address.
     * @param address The DeliveryAddress object to display in the Node.
     * @return The Node containing the address information and buttons.
     */
    private Node createAddressNode(DeliveryAddress address) {
        VBox rootVBox = setupAddressNodeStyle();

        HBox topHBox = createTopHBox(address);
        Label infoLabel = createInfoLabel();
        Label infoValue = createInfoValue(address);

        rootVBox.getChildren().addAll(topHBox, infoLabel, infoValue);
        return rootVBox;
    }


    /**
     * Sets up the style for the address node.
     * @return The VBox containing the address node with the specified style.
     */
    private VBox setupAddressNodeStyle() {
        VBox rootVBox = new VBox(5);
        rootVBox.setPadding(new Insets(10));
        rootVBox.setStyle("-fx-background-color: white; -fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10;");
        rootVBox.setPrefWidth(250);
        return rootVBox;
    }

    /**
     * Creates the top HBox for the address node.
     * @param address The DeliveryAddress object to display.
     * @return The HBox containing the address information and buttons.
     */
    private HBox createTopHBox(DeliveryAddress address) {
        HBox topHBox = new HBox(10);
        topHBox.setAlignment(Pos.CENTER_LEFT);

        VBox addressVBox = createAddressVBox(address);
        HBox buttonHBox = createButtonHBox(address);

        topHBox.getChildren().addAll(addressVBox, buttonHBox);
        return topHBox;
    }

    /**
     * Creates the info label for the address node.
     * @return The Label containing the info label.
     */
    private Label createInfoLabel() {
        Label infoLabel = new Label(bundle.getString("Info"));
        infoLabel.setStyle("-fx-font-weight: bold;");
        return infoLabel;
    }

    /**
     * Creates the info value for the address node.
     * @param address The DeliveryAddress object to display.
     * @return The Label containing the info value.
     */
    private Label createInfoValue(DeliveryAddress address) {
        String instructions = address.getInfo() != null ? address.getInfo() : bundle.getString("No_info_available");
        Label infoValue = new Label(instructions);
        infoValue.setStyle("-fx-text-fill: gray;");
        infoValue.setWrapText(true);
        return infoValue;
    }

    /**
     * Creates a VBox for displaying the address information.
     * @param address The DeliveryAddress object to display.
     * @return The VBox containing the address information.
     */
    private VBox createAddressVBox(DeliveryAddress address) {
        VBox addressVBox = new VBox(2);
        addressLabel.setText(bundle.getString("Address"));
        addressLabel.setStyle("-fx-font-weight: bold;");

        Label addressValue = new Label(address.getStreetAddress());
        addressValue.setWrapText(true);

        Label cityPostalLabel = new Label(address.getPostalCode() + ", " + address.getCity());

        addressVBox.getChildren().addAll(addressLabel, addressValue, cityPostalLabel);
        return addressVBox;
    }

    /**
     * Creates the button HBox for the address node.
     * @param address The DeliveryAddress object to display.
     * @return The HBox containing the buttons for editing, deleting, and setting the default address.
     */
    private HBox createButtonHBox(DeliveryAddress address) {
        HBox buttonHBox = new HBox(5);
        buttonHBox.setAlignment(Pos.TOP_RIGHT);

        Button editButton = createEditButton(address);
        Button deleteButton = createDeleteButton(address);
        Node defaultNode = address.isDefaultAddress() ? createDefaultIndicator() : createSetDefaultButton(address);

        buttonHBox.getChildren().addAll(editButton, deleteButton, defaultNode);
        return buttonHBox;
    }

    /**
     * Creates the Edit button for the address node.
     * @param address The DeliveryAddress object to edit.
     * @return The Button for editing the address.
     */
    private Button createEditButton(DeliveryAddress address) {
        Button editButton = new Button();
        ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/edit_icon.png")));
        editIcon.setFitWidth(16);
        editIcon.setFitHeight(16);
        editButton.setGraphic(editIcon);
        editButton.setStyle("-fx-background-color: transparent;");
        editButton.setOnAction(e -> handleEditAddress(address));
        return editButton;
    }

    /**
     * Creates the Delete button for the address node.
     * @param address The DeliveryAddress object to delete.
     * @return The Button for deleting the address.
     */
    private Button createDeleteButton(DeliveryAddress address) {
        Button deleteButton = new Button();
        ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/delete_icon.png")));
        deleteIcon.setFitWidth(16);
        deleteIcon.setFitHeight(16);
        deleteButton.setGraphic(deleteIcon);
        deleteButton.setStyle("-fx-background-color: transparent;");
        deleteButton.setOnAction(e -> handleRemoveAddress(address));
        return deleteButton;
    }

    /**
     * Creates the Default indicator for the address node.
     * @return The Node for indicating the default address.
     */
    private Node createDefaultIndicator() {
        ImageView defaultIndicator = new ImageView(new Image(getClass().getResourceAsStream("/images/star_icon.png")));
        defaultIndicator.setFitWidth(20);
        defaultIndicator.setFitHeight(20);
        Tooltip.install(defaultIndicator, new Tooltip(bundle.getString("Default_Address")));
        return defaultIndicator;
    }

    /**
     * Creates the Set Default button for the address node.
     * @param address The DeliveryAddress object to set as the default address.
     * @return The Button for setting the address as the default.
     */
    private Button createSetDefaultButton(DeliveryAddress address) {
        Button defaultButton = new Button(bundle.getString("SetAsDefault"));
        defaultButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        defaultButton.setOnAction(e -> handleSetDefaultAddress(address));
        Tooltip.install(defaultButton, new Tooltip(bundle.getString("Set_Default_Address")));
        return defaultButton;
    }

    /**
     * Handles the Add Address button action.
     * Opens the Add Address dialog for the user to input a new delivery address.
     * After the dialog is closed, refreshes the delivery addresses.
     */
    @FXML
    private void handleAddAddress() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/addAddressDialog.fxml"));
            loader.setResources(LocalizationManager.getBundle());
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(bundle.getString("Add_New_Address"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // After the dialog is closed, refresh the addresses
            fetchDeliveryAddresses();
        } catch (IOException e) {
            logger.error("Error opening add address dialog", e);
            showError(bundle.getString("Address_dialog_error"));
        }
    }

    /**
     * Handles the Edit Address button action.
     * @param address The DeliveryAddress object to edit.
     * Opens the Edit Address dialog for the user to edit the delivery address.
     */
    private void handleEditAddress(DeliveryAddress address) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/Owner/editAddressDialog.fxml"));
            loader.setResources(LocalizationManager.getBundle());
            Parent root = loader.load();

            EditAddressDialogController dialogController = loader.getController();
            dialogController.setAddress(address);

            Stage stage = new Stage();
            stage.setTitle(bundle.getString("EditAddress"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            fetchDeliveryAddresses();
        } catch (IOException e) {
            logger.error("Error opening edit address dialog", e);
            showError(bundle.getString("Edit_address_dialog_error"));
        }
    }

    /**
     * Handles the Remove Address button action.
     * @param address The DeliveryAddress object to remove.
     * Prompts the user to confirm the deletion of the address.
     * If the user confirms, deletes the address from the database.
     * After the address is deleted, refreshes the delivery addresses.
     */
    private void handleRemoveAddress(DeliveryAddress address) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("DeleteAddress"));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString("Delete_confirmation_message"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.runLater(() -> {
                deliveryAddressesList.remove(address);
                deleteAddress(address);
                updateDeliveryAddressesUI();
            });

        }
    }

    /**
     * Handles the Set Default Address button action.
     * @param address The DeliveryAddress object to set as the default address.
     * Prompts the user to confirm setting the address as the default.
     * If the user confirms, sets the address as the default in the database.
     */
    @FXML
    private void handleSetDefaultAddress(DeliveryAddress address) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("Set_Default_Address"));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString("Set_address_confirmation_message"));
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Proceed to set as default
            Task<Void> task = deliveryAddressService.setDefaultAddress(address, userId);

            task.setOnSucceeded(e -> updateDefaultAddressInUI(address));

            task.setOnFailed(e -> {
                Throwable exception = task.getException();
                logger.error("Error setting default address", exception);
                showError(bundle.getString("Set_default_address_error"));
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

    /**
     * Updates the UI with the new default address.
     * @param newDefaultAddress The DeliveryAddress object that is set as the new default address.
     * Sets the isDefaultAddress property for the new default address to true.
     */
    private void updateDefaultAddressInUI(DeliveryAddress newDefaultAddress) {
        // Update the isDefaultAddress property of all addresses
        for (DeliveryAddress addr : deliveryAddressesList) {
            addr.setDefaultAddress(addr.getDeliveryAddressId() == newDefaultAddress.getDeliveryAddressId());
        }

        // Refresh the UI nodes
        Platform.runLater(() -> {
            deliveryAddressContainer.getChildren().clear();
            for (DeliveryAddress address : deliveryAddressesList) {
                Node addressNode = createAddressNode(address);
                deliveryAddressContainer.getChildren().add(addressNode);
            }
        });
    }

    /**
     * Deletes the delivery address from the database.
     * @param address The DeliveryAddress object to delete.
     */
    private void deleteAddress(DeliveryAddress address) {
        Task<Void> task = deliveryAddressService.deleteAddress(address.getDeliveryAddressId());

        task.setOnSucceeded(event -> Platform.runLater(this::fetchDeliveryAddresses));

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            logger.error("Error deleting address", exception);
            Platform.runLater(() -> showError(bundle.getString("FailToDelete")));
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Handles the Edit Contact Info button action.
     * Opens the contact info dialog for the user to edit their contact information.
     */
    @FXML
    private void handleEditContactInfo() {
        openContactInfoDialog(currentContactInfo);
    }

    /**
     * Handles the Change Password button action.
     * @param actionEvent The ActionEvent object that triggered the event.
     * Opens the change password dialog for the user to change their password.
     */
    @FXML
    public void handleChangePassword(ActionEvent actionEvent) {
        openEditPasswordDialog();
    }

    /**
     * Opens the change password dialog.
     */
    private void openEditPasswordDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/editPasswordDialog.fxml"));
            loader.setResources(LocalizationManager.getBundle());
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(bundle.getString("ChangePassword"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            fetchUserData();

        } catch (IOException e) {
            logger.error("Error opening change password dialog", e);
            showError(bundle.getString("Open_changePW_dialog"));
        }
    }

    /**
     * Handles the Delete Account button action.
     * Prompts the user to confirm the deletion of their account.
     * If the user confirms, deletes the account from the database.
     */
    public void handleDeleteAccount() {
        // Create a confirmation dialog
        Dialog<String> confirmationDialog = new Dialog<>();
        confirmationDialog.setTitle(bundle.getString("Delete_account_confirm_title")); // Localized title
        confirmationDialog.setHeaderText(bundle.getString("Delete_account_confirm_message")); // Localized message

        // Set the button types
        ButtonType okButtonType = new ButtonType(bundle.getString("OK"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(bundle.getString("Cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationDialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        // Create a text field for user input
        TextField inputField = new TextField();
        inputField.setPromptText(bundle.getString("Enter_DELETE_to_confirm")); // Localized prompt text

        // Add the text field to the dialog
        VBox content = new VBox();
        content.setSpacing(10);
        content.getChildren().add(inputField);
        confirmationDialog.getDialogPane().setContent(content);

        // Enable the OK button only if the user enters "DELETE"
        Node okButton = confirmationDialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);
        inputField.textProperty().addListener((observable, oldValue, newValue) ->
                okButton.setDisable(!"DELETE".equals(newValue.trim())));

        // Process the result
        confirmationDialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return inputField.getText();
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<String> result = confirmationDialog.showAndWait();
        if (result.isPresent()) {
            if ("DELETE".equals(result.get())) {
                // Proceed with account deletion
                Task<Void> deleteTask = userService.deleteUser();

                deleteTask.setOnSucceeded(event -> {
                    DropDownMenuController dropDownMenuController = new DropDownMenuController();
                    dropDownMenuController.handleLogout();
                    // Show a success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(bundle.getString("Delete_account_success_title")); // Localized title
                    alert.setHeaderText(null);
                    alert.setContentText(bundle.getString("Delete_account_success_message")); // Localized message
                    alert.showAndWait();
                });

                deleteTask.setOnFailed(event -> {
                    // Show an error message
                    Throwable exception = deleteTask.getException();
                    if (exception != null) {
                        showError(bundle.getString("Delete_account_error") + ": " + exception.getMessage());
                    } else {
                        showError(bundle.getString("Delete_account_unknown_error"));
                    }
                });

                // Run the task on a background thread
                new Thread(deleteTask).start();
            } else {
                // Show an error alert if "DELETE" was not entered correctly
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("Delete_account_error_title")); // Localized title
                alert.setHeaderText(null);
                alert.setContentText(bundle.getString("Delete_account_error_incorrect_message")); // Localized error message
                alert.showAndWait();
            }
        } else {
            // Show an alert if the user cancels the dialog
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(bundle.getString("Delete_account_cancel_title")); // Localized title
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("Delete_account_cancel_message")); // Localized message
            alert.showAndWait();
        }
    }

}


