package ofosFrontend.controller.Owner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Pair;
import ofosFrontend.model.Restaurant;
import ofosFrontend.service.RestaurantService;
import ofosFrontend.session.LocalizationManager;

import java.io.*;
import java.util.prefs.Preferences;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminMainMenuController extends AdminBasicController {
    @FXML
    private VBox restaurantsVBox;
    @FXML
    private VBox restaurantListVBox;
    @FXML
    private Label addressLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label hoursLabel;

    @FXML
    private Text defaultText;

    private RestaurantService restaurantService = new RestaurantService();
    private Restaurant currentSelectedRestaurant;

    private static final String LAST_SELECTED_RESTAURANT_KEY = "lastSelectedRestaurant";

    private Preferences prefs = Preferences.userNodeForPackage(AdminMainMenuController.class);

    @FXML
    public void initialize() {
        loadRestaurants();
    }

    public AdminMainMenuController() {
        // Required by FXML loader
    }

    public void loadRestaurants() {
        try {
            restaurantListVBox.getChildren().clear();

            List<Restaurant> restaurants = restaurantService.getOwnerRestaurants();

            int lastSelectedRestaurantId = prefs.getInt(LAST_SELECTED_RESTAURANT_KEY, -1);
            boolean isRestaurantSelected = false;

            for (Restaurant restaurant : restaurants) {
                HBox restaurantBox = new HBox();
                restaurantBox.setSpacing(10.0);
                restaurantBox.setStyle("-fx-padding: 5px; -fx-background-color: #e8f4fb; -fx-border-color: #000;");

                Text restaurantNameText = new Text(restaurant.getRestaurantName());
                restaurantNameText.setFont(Font.font(12));
                restaurantNameText.setStyle("-fx-font-weight: bold;");

                restaurantBox.getChildren().add(restaurantNameText);

                restaurantBox.setOnMouseClicked(event -> {
                    currentSelectedRestaurant = restaurant;

                    prefs.putInt(LAST_SELECTED_RESTAURANT_KEY, restaurant.getId());

                    updateRestaurantDetailsUI();
                });

                restaurantListVBox.getChildren().add(restaurantBox);

                if (restaurant.getId() == lastSelectedRestaurantId) {
                    currentSelectedRestaurant = restaurant;
                    isRestaurantSelected = true;
                }
            }

            if (!isRestaurantSelected && !restaurants.isEmpty()) {
                currentSelectedRestaurant = restaurants.get(0);
                prefs.putInt(LAST_SELECTED_RESTAURANT_KEY, currentSelectedRestaurant.getId());
            }

            updateRestaurantDetailsUI();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateRestaurantDetailsUI() {
        if (currentSelectedRestaurant != null) {
            defaultText.setText(currentSelectedRestaurant.getRestaurantName());
            addressLabel.setText(currentSelectedRestaurant.getAddress());
            phoneLabel.setText(currentSelectedRestaurant.getRestaurantPhone());
            hoursLabel.setText(currentSelectedRestaurant.getBusinessHours());
        }
    }

    @FXML
    private void modifyRestaurantInfo() {
        ResourceBundle bundle = LocalizationManager.getBundle();
        String alertTitle = bundle.getString("NoRestaurantSelected");
        String alertHeader = bundle.getString("SelectRestaurantToModify");
        String dialogTitle = bundle.getString("ModifyTitle");
        String dialogHeader = bundle.getString("ModifyHeader");
        String dialogAddress = bundle.getString("DialogAddress");
        String dialogPhone = bundle.getString("DialogPhone");
        String dialogHours = bundle.getString("DialogHours");
        String saveButton = bundle.getString("SaveButton");
        String cancelButton = bundle.getString("CancelButton");
        String infoUpdated = bundle.getString("InfoUpdated");
        String infoHeader = bundle.getString("InfoUpdateHeader");
        String errorTitle = bundle.getString("ErrorTitle");
        String infoFailure = bundle.getString("InfoFailure");
        String errorContext = bundle.getString("ErrorContext");


        if (currentSelectedRestaurant == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(alertTitle);
            alert.setHeaderText(alertHeader);
            alert.showAndWait();
            return;
        }

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);
        dialog.setHeaderText(dialogHeader);

        ButtonType modifyButtonType = new ButtonType(saveButton, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE); // Localized cancel button
        dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, cancelButtonType);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField addressField = new TextField(currentSelectedRestaurant.getAddress());
        TextField phoneField = new TextField(currentSelectedRestaurant.getRestaurantPhone());
        TextField hoursField = new TextField(currentSelectedRestaurant.getBusinessHours());

        grid.add(new Label(dialogAddress), 0, 0);
        grid.add(addressField, 1, 0);
        grid.add(new Label(dialogPhone), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label(dialogHours), 0, 2);
        grid.add(hoursField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                return new Pair<>(addressField.getText(), phoneField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(info -> {
            currentSelectedRestaurant.setAddress(addressField.getText());
            currentSelectedRestaurant.setRestaurantPhone(phoneField.getText());
            currentSelectedRestaurant.setHours(hoursField.getText());

            updateRestaurantDetailsUI();

            try {
                restaurantService.updateRestaurantInfo(currentSelectedRestaurant);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(infoUpdated);
                alert.setHeaderText(infoHeader);
                alert.setContentText(dialogAddress + ": " + addressField.getText() +
                        "\n" + dialogPhone + ": " + phoneField.getText() + "\n" +
                        dialogHours + ": " + hoursField.getText());
                alert.showAndWait();
            } catch (IOException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle(errorTitle);
                errorAlert.setHeaderText(infoFailure);
                errorAlert.setContentText(errorContext);
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void goToEditMenu(ActionEvent actionEvent) {
        mainController.loadRestaurantContent(currentSelectedRestaurant);

    }
}
