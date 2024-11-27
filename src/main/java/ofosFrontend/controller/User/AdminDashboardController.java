package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ofosFrontend.model.Restaurant;
import ofosFrontend.model.User;
import ofosFrontend.service.RestaurantService;
import ofosFrontend.service.UserService;
import ofosFrontend.session.LocalizationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;


@SuppressWarnings({"checkstyle:MissingJavadocType", "checkstyle:Indentation"})
public class AdminDashboardController {

    @FXML
    private ComboBox<String> bannedUserSelector;
    @FXML
    private Label userEnabledLabel;
    @FXML
    private Label userIDLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userRoleLabel;
    @FXML
    private Label restaurantOwnerLabel;
    @FXML
    private Label restaurantIDLabel;
    @FXML
    private Label restaurantNameLabel;
    @FXML
    private Label banUserNameLabel;
    @FXML
    private Label banUserIDLabel;
    @FXML
    private Label banUserEnabledLabel;


    @FXML
    private ComboBox<String> restaurantSelector;

    @FXML
    private ComboBox<String> userSelector;

    private final RestaurantService restaurantService = new RestaurantService();
    private final UserService userService = new UserService();
    private List<Restaurant> restaurants;
    private Restaurant selectedRestaurant;
    private User selectedUser;
    private static final String CANCEL_TITLE = "CancelTitle";
    private static final String ERROR_HEADER = "ErrorHeader";
    private static final String USER_ID = "User_ID";
    private static final String USERNAME = "User_Name";
    private static final String NO_USER_SELECTED_KEY = "NoUserSelected";
    private static final String SUCCESS_TITLE_KEY = "SuccessTitle";
    private static final String ERROR_TITLE_KEY = "ErrorTitle";
    private static final String FAILED_TO_LOAD_USER = "Failed_to_load_user";
    private final Logger logger = LogManager.getLogger(this.getClass());
    ResourceBundle bundle = LocalizationManager.getBundle();


    @FXML
    public void initialize() {
        loadRestaurants();
        loadUsers();
        loadBannedUsers();

        // Add listeners
        restaurantSelector.setOnAction(event -> displaySelectedRestaurant());
        userSelector.setOnAction(event -> displaySelectedUser());
        bannedUserSelector.setOnAction(event -> displayBannedUser());
    }

    private void loadUsers() {
        userSelector.getItems().clear();
        try {
            List<User> users = userService.getAllUsers();

            for (User user : users) {
                if (user.getEnabled())
                    userSelector.getItems().add(user.getUsername());
            }

        } catch (IOException e) {
            showError(bundle.getString(FAILED_TO_LOAD_USER));
            e.printStackTrace();
        }
    }

    private void loadBannedUsers() {
        bannedUserSelector.getItems().clear();
        try {
            List<User> users = userService.getAllUsers();

            for (User user : users) {
                if (!user.getEnabled()) {
                    bannedUserSelector.getItems().add(user.getUsername());
                }
            }

        } catch (IOException e) {
            showError(bundle.getString(FAILED_TO_LOAD_USER));
            e.printStackTrace();
        }
    }

    private void loadRestaurants() {
        try {
            restaurants = restaurantService.getAllRestaurants();

            for (Restaurant restaurant : restaurants) {
                restaurantSelector.getItems().add(restaurant.getRestaurantName());
            }

        } catch (IOException e) {
            showError(bundle.getString("Failed_to_load_restaurants"));
            e.printStackTrace();
        }
    }

    private void displaySelectedRestaurant() {
        String selectedRestaurantName = restaurantSelector.getValue();

        if (selectedRestaurantName != null) {
            selectedRestaurant = restaurants.stream()
                    .filter(restaurant -> restaurant.getRestaurantName().equals(selectedRestaurantName))
                    .findFirst()
                    .orElse(null);

            if (selectedRestaurant != null) {
                restaurantNameLabel.setText(bundle.getString("Restaurant_Name") + selectedRestaurant.getRestaurantName());
                restaurantIDLabel.setText(bundle.getString("Restaurant_ID") + selectedRestaurant.getId());
                restaurantOwnerLabel.setText(bundle.getString("Owner") + ": " + selectedRestaurant.getOwnerUsername());
            } else {
                showError(bundle.getString("Failed_to_load_restaurants"));
            }
        }
    }

    private void displaySelectedUser() {
        String selectedUserName = userSelector.getValue();

        if (selectedUserName != null) {
            try {
                selectedUser = userService.getUserByUsername(selectedUserName);

                if (selectedUser != null) {
                    userNameLabel.setText(bundle.getString(USERNAME) + selectedUser.getUsername());
                    userIDLabel.setText(bundle.getString(USER_ID) + selectedUser.getUserId());
                    userRoleLabel.setText(bundle.getString("Role") + ": " + selectedUser.getRole());
                    userEnabledLabel.setText(bundle.getString("Enabled") + ": " + selectedUser.getEnabled());
                } else {
                    showError(bundle.getString(FAILED_TO_LOAD_USER));
                }
            } catch (IOException e) {
                showError(bundle.getString(FAILED_TO_LOAD_USER));
                e.printStackTrace();
            }
        }
    }

    public void displayBannedUser() {
        String selectedUserName = bannedUserSelector.getValue();

        if (selectedUserName != null) {
            try {
                User bannedSelectedUser = userService.getUserByUsername(selectedUserName);

                if (bannedSelectedUser != null) {
                    banUserNameLabel.setText(bundle.getString(USERNAME) + bannedSelectedUser.getUsername());
                    banUserIDLabel.setText(bundle.getString(USER_ID) + bannedSelectedUser.getUserId());
                    banUserEnabledLabel.setText(bundle.getString("Enabled") + ": " + bannedSelectedUser.getEnabled());
                } else {
                    showError(bundle.getString(FAILED_TO_LOAD_USER));
                }
            } catch (IOException e) {
                showError(bundle.getString(FAILED_TO_LOAD_USER));
                e.printStackTrace();
            }
        }
    }

    public void banUser() {
        String selectedUserName = userSelector.getValue();
        if (selectedUserName == null) {
            showError(bundle.getString(NO_USER_SELECTED_KEY));
            return;
        }

        try {
            selectedUser = userService.getUserByUsername(selectedUserName);

            if (selectedUser != null) {
                // Confirmation dialog
                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle(bundle.getString("BanUserTitle"));
                confirmationDialog.setHeaderText(bundle.getString("BanUserHeader"));
                confirmationDialog.setContentText(bundle.getString("BanUserContent") + "\n\n" +
                        bundle.getString(USERNAME) + selectedUser.getUsername() + "\n" +
                        bundle.getString(USER_ID) + selectedUser.getUserId());

                // Show the dialog and wait for user response
                ButtonType result = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);

                // If user clicks OK, proceed with the operation
                if (result == ButtonType.OK) {
                    boolean success = userService.banUser(selectedUser.getUserId());

                    if (success) {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle(bundle.getString(SUCCESS_TITLE_KEY));
                        successAlert.setHeaderText(bundle.getString("BanSuccessHeader"));
                        successAlert.setContentText(bundle.getString("UserBanned") + "\n" +
                                bundle.getString(USERNAME) + ": " + selectedUser.getUsername());
                        successAlert.showAndWait();
                    } else {
                        Alert failureAlert = new Alert(Alert.AlertType.ERROR);
                        failureAlert.setTitle(bundle.getString(ERROR_TITLE_KEY));
                        failureAlert.setHeaderText(bundle.getString(ERROR_HEADER));
                        failureAlert.setContentText(bundle.getString("BanFailed"));
                        failureAlert.showAndWait();
                    }
                } else {
                    Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                    cancelAlert.setTitle(bundle.getString(CANCEL_TITLE));
                    cancelAlert.setContentText(bundle.getString("BanCanceled"));
                    cancelAlert.showAndWait();
                    logger.info("Ban operation canceled.");
                }
            }
        } catch (IOException e) {
            showError(bundle.getString(FAILED_TO_LOAD_USER));
            e.printStackTrace();
        }

        loadUsers();
        loadBannedUsers();
    }

    public void changeOwner() throws IOException {
        if (selectedRestaurant == null) {
            showError(bundle.getString("NoRestaurantSelected"));
            return;
        }

        if (selectedUser == null) {
            showError(bundle.getString(NO_USER_SELECTED_KEY));
            return;
        }

        String currentOwner = selectedRestaurant.getOwnerUsername();
        String newOwner = selectedUser.getUsername();
        int newOwnerId = selectedUser.getUserId();


        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle(bundle.getString("ChangeOwnerTitle"));
        confirmationDialog.setHeaderText(bundle.getString("ChangeOwnerHeader"));
        confirmationDialog.setContentText(bundle.getString("ChangeOwnerContent") + "\n\n" +
                bundle.getString("Restaurant") + ": " + selectedRestaurant.getRestaurantName() + "\n" +
                bundle.getString("CurrentOwner") + currentOwner + "\n" +
                bundle.getString("NewOwner") + newOwner);


        ButtonType result = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);

        // If user clicks OK, proceed with the operation
        if (result == ButtonType.OK) {
            try {
                boolean success = restaurantService.changeOwner(selectedRestaurant.getId(), newOwnerId);

                if (success) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle(bundle.getString(SUCCESS_TITLE_KEY));
                    successAlert.setHeaderText(bundle.getString("SuccessHeader"));
                    successAlert.setContentText(bundle.getString("OwnerChanged") + "\n" +
                            bundle.getString("Restaurant") + selectedRestaurant.getRestaurantName() + "\n" +
                            bundle.getString("NewOwner") + newOwner);
                    successAlert.showAndWait();
                }
            } catch (IOException e) {
                Alert failureAlert = new Alert(Alert.AlertType.ERROR);
                failureAlert.setTitle(bundle.getString(ERROR_TITLE_KEY));
                failureAlert.setHeaderText(bundle.getString(ERROR_HEADER));
                failureAlert.setContentText(bundle.getString("ChangeOwnerFailed") + "\n\n" + e.getMessage());
                failureAlert.showAndWait();
                e.printStackTrace();
            }
        } else {
            Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
            cancelAlert.setTitle(bundle.getString(CANCEL_TITLE));
            cancelAlert.setHeaderText(bundle.getString("CancelHeader"));
            cancelAlert.setContentText(bundle.getString("ChangeOwnerCanceled"));
            cancelAlert.showAndWait();
            logger.info("Change owner operation canceled.");
        }
    }

    public void unbanUser() {
        String selectedUserName = bannedUserSelector.getValue();
        if (selectedUserName == null) {
            showError(bundle.getString(NO_USER_SELECTED_KEY));
            return;
        }

        try {
            selectedUser = userService.getUserByUsername(selectedUserName);

            if (selectedUser != null) {
                // Confirmation dialog
                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle(bundle.getString("UnbanUserTitle"));
                confirmationDialog.setHeaderText(bundle.getString("UnbanUserHeader"));
                confirmationDialog.setContentText(bundle.getString("UnbanUserContent") + "\n\n" +
                        bundle.getString(USERNAME) + selectedUser.getUsername() + "\n" +
                        bundle.getString(USER_ID) + selectedUser.getUserId());

                ButtonType result = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);

                if (result == ButtonType.OK) {
                    boolean success = userService.banUser(selectedUser.getUserId());

                    if (success) {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle(bundle.getString(SUCCESS_TITLE_KEY));
                        successAlert.setHeaderText(bundle.getString("UnbanSuccessHeader"));
                        successAlert.setContentText(bundle.getString("UserUnbanned") + "\n" +
                                bundle.getString(USERNAME) + ": " + selectedUser.getUsername());
                        successAlert.showAndWait();
                    } else {
                        Alert failureAlert = new Alert(Alert.AlertType.ERROR);
                        failureAlert.setTitle(bundle.getString(ERROR_TITLE_KEY));
                        failureAlert.setHeaderText(bundle.getString(ERROR_HEADER));
                        failureAlert.setContentText(bundle.getString("UnbanFailed"));
                        failureAlert.showAndWait();
                    }
                } else {
                    Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                    cancelAlert.setTitle(bundle.getString(CANCEL_TITLE));
                    cancelAlert.setContentText(bundle.getString("UnbanCanceled"));
                    cancelAlert.showAndWait();
                    logger.info("Unban operation canceled.");
                }
            }
        } catch (IOException e) {
            showError(bundle.getString(FAILED_TO_LOAD_USER));
            e.printStackTrace();
        }

        loadUsers();
        loadBannedUsers();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void addRestaurant() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/addRestaurantDialog.fxml"));
            loader.setResources(LocalizationManager.getBundle());
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(bundle.getString("Add_a_restaurant"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeRole() {
        if (selectedUser == null) {
            showError(bundle.getString(NO_USER_SELECTED_KEY));
            return;
        }

        // Create the dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(bundle.getString("ChangeRoleTitle"));
        dialog.setHeaderText(bundle.getString("ChangeRoleHeader"));

        // Create dialog content
        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 10;");

        Label usernameLabel = new Label(bundle.getString("Username") + ": " + selectedUser.getUsername());
        Label userIdLabel = new Label(bundle.getString("UserID") + ": " + selectedUser.getUserId());
        Label currentRoleLabel = new Label(bundle.getString("CurrentRole") + ": " + selectedUser.getRole());

        ComboBox<String> newRoleSelector = new ComboBox<>();
        newRoleSelector.getItems().addAll("ADMIN", "OWNER", "USER"); // Add available roles
        newRoleSelector.setPromptText(bundle.getString("SelectNewRole"));

        content.getChildren().addAll(usernameLabel, userIdLabel, currentRoleLabel, newRoleSelector);

        // Add content to the dialog
        dialog.getDialogPane().setContent(content);

        // Add buttons to the dialog
        ButtonType confirmButton = new ButtonType(bundle.getString("Confirm"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType(bundle.getString("Cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, cancelButton);

        // Show the dialog and wait for the result
        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == confirmButton) {
                String newRole = newRoleSelector.getValue();
                if (newRole == null || newRole.isEmpty()) {
                    showError(bundle.getString("RoleNotSelected"));
                    return;
                }

                logger.info("Changing role of user: {} with ID: {} to new role: {}",
                        selectedUser.getUsername(),
                        selectedUser.getUserId(),
                        newRole);

                // Call the userService to update the role
                boolean isChanged = userService.changeRole(selectedUser.getUserId(), newRole);

                // Show success or error message
                if (isChanged) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle(bundle.getString(SUCCESS_TITLE_KEY));
                    successAlert.setHeaderText(null);
                    successAlert.setContentText(bundle.getString("RoleChangeSuccess"));
                    successAlert.showAndWait();
                } else {
                    Alert failureAlert = new Alert(Alert.AlertType.ERROR);
                    failureAlert.setTitle(bundle.getString(ERROR_TITLE_KEY));
                    failureAlert.setHeaderText(null);
                    failureAlert.setContentText(bundle.getString("RoleChangeFailed"));
                    failureAlert.showAndWait();
                }
            }
        });
    }

}
