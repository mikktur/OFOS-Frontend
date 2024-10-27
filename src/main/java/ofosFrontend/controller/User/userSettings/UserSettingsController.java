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
import ofosFrontend.model.ContactInfo;
import ofosFrontend.model.DeliveryAddress;
import ofosFrontend.service.DeliveryAddressService;
import ofosFrontend.service.UserService;
import ofosFrontend.session.SessionManager;

import java.io.IOException;
import java.util.*;

public class UserSettingsController extends BasicController {

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneNumberLabel;
    @FXML private Label cityLabel;
    @FXML private Label postalCodeLabel;
    @FXML private Label addressLabel;
    @FXML private VBox deliveryAddressContainer;
    @FXML private Button changePassword;

    private int userId;
    private List<DeliveryAddress> deliveryAddressesList = new ArrayList<>();
    private final DeliveryAddressService deliveryAddressService = new DeliveryAddressService();
    private final UserService userService = new UserService();

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

        changePassword.setOnMouseExited(event -> {
            Platform.runLater(() -> {
                rotate.stop();
                changePassword.setRotate(0);
            });
        });
    }

    private void fetchUserData() {
        int userId = SessionManager.getInstance().getUserId();
        Task<ContactInfo> task = userService.fetchUserData(userId);

        task.setOnSucceeded(event -> {
            ContactInfo contactInfo = task.getValue();

            if (contactInfo != null) {
                // Update UI with contact information
                Platform.runLater(() -> {
                    nameLabel.setText(contactInfo.getFirstName() + " " + contactInfo.getLastName());
                    emailLabel.setText(contactInfo.getEmail());
                    phoneNumberLabel.setText(contactInfo.getPhoneNumber());
                    addressLabel.setText(contactInfo.getAddress());
                    cityLabel.setText(contactInfo.getCity());
                    postalCodeLabel.setText(contactInfo.getPostalCode());
                });
            } else {
                // No contact information found, prompt user to enter it
                Platform.runLater(this::promptForContactInfo);
            }
        });

        task.setOnFailed(event -> {
            Throwable e = task.getException();
            e.printStackTrace();
            Platform.runLater(() -> {
                showError("An error occurred while fetching contact information.");
            });
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void promptForContactInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Contact Information");
        alert.setHeaderText(null);
        alert.setContentText("You have not set up your contact information yet. Would you like to add it now?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            // Open the dialog to add contact information
            openContactInfoDialog();
        } else {
            // User chose not to add contact information now
            // Update the UI accordingly
            Platform.runLater(() -> {
                nameLabel.setText("No name available");
                emailLabel.setText("No email available");
                phoneNumberLabel.setText("No phone number available");
                cityLabel.setText("No city available");
                postalCodeLabel.setText("No postal code available");
                addressLabel.setText("No address available");
            });
        }
    }

    private void openContactInfoDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/contactInfoDialog.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Contact Information");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // After the dialog is closed, fetch the contact information again
            fetchUserData();

        } catch (IOException e) {
            e.printStackTrace();
            showError("An error occurred while opening the contact information dialog.");
        }
    }



    private void fetchDeliveryAddresses() {
        Task<List<DeliveryAddress>> task = deliveryAddressService.fetchDeliveryAddresses(userId);

        task.setOnSucceeded(event -> {
            deliveryAddressesList = task.getValue();
            updateDeliveryAddressesUI();
        });

        task.setOnFailed(event -> {
            Throwable e = task.getException();
            e.printStackTrace();
            showError("An error occurred while retrieving delivery addresses.");
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }


    private void updateDeliveryAddressesUI() {
        Platform.runLater(() -> {
            deliveryAddressContainer.getChildren().clear();

            if (deliveryAddressesList.isEmpty()) {
                // If there are no addresses, add the placeholder Label
                Label placeholderLabel = new Label("No saved delivery addresses");
                placeholderLabel.setStyle("-fx-font-style: italic; -fx-text-fill: gray;");
                placeholderLabel.setPadding(new Insets(10));
                deliveryAddressContainer.getChildren().add(placeholderLabel);
            } else {
                for (DeliveryAddress address : deliveryAddressesList) {
                    Node addressNode = createAddressNode(address);
                    deliveryAddressContainer.getChildren().add(addressNode);
                }
            }
        });
    }




    private Node createAddressNode(DeliveryAddress address) {
        // Root VBox for each address node
        VBox rootVBox = new VBox(5);
        rootVBox.setPadding(new Insets(10));
        rootVBox.setStyle("-fx-background-color: white; -fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10;");
        rootVBox.setPrefWidth(250);

        // Top HBox containing Address information and buttons
        HBox topHBox = new HBox(10);
        topHBox.setAlignment(Pos.CENTER_LEFT);

        // VBox for Address labels and values
        VBox addressVBox = new VBox(2);
        Label addressLabel = new Label("Address:");
        addressLabel.setStyle("-fx-font-weight: bold;");
        Label addressValue = new Label(address.getStreetAddress());

        Label cityPostalLabel = new Label(address.getPostalCode() + ", " + address.getCity());
        addressVBox.getChildren().addAll(addressLabel, addressValue, cityPostalLabel);

        // HBox for Buttons
        HBox buttonHBox = new HBox(5);
        buttonHBox.setAlignment(Pos.TOP_RIGHT);

        // Edit Button
        Button editButton = new Button();
        ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/edit_icon.png")));
        editIcon.setFitWidth(16);
        editIcon.setFitHeight(16);
        editButton.setGraphic(editIcon);
        editButton.setOnAction(e -> handleEditAddress(address));
        editButton.setStyle("-fx-background-color: transparent;");

        // Delete Button
        Button deleteButton = new Button();
        ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/delete_icon.png")));
        deleteIcon.setFitWidth(16);
        deleteIcon.setFitHeight(16);
        deleteButton.setGraphic(deleteIcon);
        deleteButton.setOnAction(e -> handleRemoveAddress(address));
        deleteButton.setStyle("-fx-background-color: transparent;");

        // Create defaultNode
        Node defaultNode;
        if (address.isDefaultAddress()) {
            // Default Indicator
            ImageView defaultIndicator = new ImageView(new Image(getClass().getResourceAsStream("/images/star_icon.png")));
            defaultIndicator.setFitWidth(20);
            defaultIndicator.setFitHeight(20);
            Tooltip.install(defaultIndicator, new Tooltip("Default Address"));
            defaultNode = defaultIndicator;
        } else {
            // Set as Default Button
            Button defaultButton = new Button();
            defaultButton.setText("Set as Default");
            defaultButton.setOnAction(e -> handleSetDefaultAddress(address));
            defaultButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-border-radius: 5; -fx-background-radius: 5;");
            Tooltip.install(defaultButton, new Tooltip("Set as Default Address"));
            defaultNode = defaultButton;
        }

        // Add tooltips
        Tooltip.install(editButton, new Tooltip("Edit Address"));
        Tooltip.install(deleteButton, new Tooltip("Delete Address"));

        // Add buttons to buttonHBox
        buttonHBox.getChildren().addAll(editButton, deleteButton, defaultNode);

        HBox.setHgrow(addressVBox, Priority.ALWAYS);

        // Add addressVBox and buttonHBox to topHBox
        topHBox.getChildren().addAll(addressVBox, buttonHBox);

        // Info Label and Value
        Label infoLabel = new Label("Info:");
        infoLabel.setStyle("-fx-font-weight: bold;");
        String instructions = address.getInfo() != null ? address.getInfo() : "";
        Label infoValue = new Label(instructions);
        infoValue.setStyle("-fx-text-fill: gray;");
        infoValue.setWrapText(true);

        // Add components to rootVBox
        rootVBox.getChildren().addAll(topHBox, infoLabel, infoValue);

        return rootVBox;
    }




    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleAddAddress() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/addAddressDialog.fxml"));
            Parent root = loader.load();

            AddAddressDialogController dialogController = loader.getController();
            dialogController.setUserId(userId);

            Stage stage = new Stage();
            stage.setTitle("Add New Address");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // After the dialog is closed, refresh the addresses
            fetchDeliveryAddresses();
        } catch (IOException e) {
            e.printStackTrace();
            showError("An error occurred while opening the add address dialog.");
        }
    }


    private void handleEditAddress(DeliveryAddress address) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/Owner/editAddressDialog.fxml"));
            Parent root = loader.load();

            EditAddressDialogController dialogController = loader.getController();
            dialogController.setAddress(address);

            Stage stage = new Stage();
            stage.setTitle("Edit Address");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            fetchDeliveryAddresses();
        } catch (IOException e) {
            e.printStackTrace();
            showError("An error occurred while opening the edit address dialog.");
        }
    }


    private void handleRemoveAddress(DeliveryAddress address) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Address");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this address?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.runLater(() -> {
                deliveryAddressesList.remove(address);
                deleteAddress(address);
                updateDeliveryAddressesUI();
            });

        }
    }

    @FXML
    private void handleSetDefaultAddress(DeliveryAddress address) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Set as Default Address");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to set this address as your default?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Proceed to set as default
            Task<Void> task = deliveryAddressService.setDefaultAddress(address, userId);

            task.setOnSucceeded(e -> {
                // Update the UI
                updateDefaultAddressInUI(address);  // Refresh the list of addresses
            });

            task.setOnFailed(e -> {
                Throwable exception = task.getException();
                exception.printStackTrace();
                showError("An error occurred while setting the default address.");
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

    private void updateDefaultAddressInUI(DeliveryAddress newDefaultAddress) {
        // Update the isDefaultAddress property of all addresses
        for (DeliveryAddress addr : deliveryAddressesList) {
            if (addr.getDeliveryAddressId() == newDefaultAddress.getDeliveryAddressId()) {
                addr.setDefaultAddress(true);
            } else {
                addr.setDefaultAddress(false);
            }
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



    private void deleteAddress(DeliveryAddress address) {
        Task<Void> task = deliveryAddressService.deleteAddress(address.getDeliveryAddressId());

        task.setOnSucceeded(event -> {
            // Refresh the delivery addresses after successful deletion
            Platform.runLater(this::fetchDeliveryAddresses);
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
            Platform.runLater(() -> {
                showError("Failed to delete address.");
            });
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void handleEditContactInfo() {
        openContactInfoDialog();
    }

    @FXML
    public void handleChangePassword(ActionEvent actionEvent) {
        openEditPasswordDialog();
    }

    private void openEditPasswordDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/editPasswordDialog.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Change Password");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            fetchUserData();

        } catch (IOException e) {
            e.printStackTrace();
            showError("An error occurred while opening the change password dialog.");
        }
    }


}


