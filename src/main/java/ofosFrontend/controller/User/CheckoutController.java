package ofosFrontend.controller.User;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ofosFrontend.controller.User.userSettings.AddAddressDialogController;
import ofosFrontend.model.*;
import ofosFrontend.service.DeliveryAddressService;
import ofosFrontend.service.OrderService;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import ofosFrontend.service.CheckoutService;


public class CheckoutController  extends BasicController {
    @FXML
    VBox summaryContainer;
    @FXML
    Label subTotal;
    @FXML
    ChoiceBox<String> deliveryAddresses;
    @FXML
    ChoiceBox<String> paymentMethod;
    @FXML
    Button orderBtn;
    @FXML
    Button addAddressBtn;
    private int rid;

    ResourceBundle bundle = LocalizationManager.getBundle();
    CheckoutService checkoutService = new CheckoutService();
    private List<DeliveryAddress> deliveryAddressesList = new ArrayList<>();
    private DeliveryAddress selectedAddress;
    private String selectedPaymentMethod;
    private DeliveryAddressService deliveryAddressService = new DeliveryAddressService();
    private int userId = SessionManager.getInstance().getUserId();
    private OrderService orderService = new OrderService();

    public CheckoutController() {
    }

    @FXML
    public void initialize() {

        //renderSummary();
        getDeliveryAddresses();
        populatePaymentMethods();
        setupListeners();
    }

    public void setupListeners() {

        // Set up the listener for delivery addresses
        deliveryAddresses.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 0) {
                selectedAddress = deliveryAddressesList.get(newValue.intValue());
            }
        });

        // Set up the listener for payment methods
        paymentMethod.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 0) {
                selectedPaymentMethod = paymentMethod.getItems().get(newValue.intValue());
            }
        });

        // Set up the action for the order and add address buttons

            orderBtn.setOnAction(event -> openConfirmationDialog());
        addAddressBtn.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/User/addAddressDialog.fxml"));
                loader.setResources(LocalizationManager.getBundle());
                Parent root = loader.load();

                AddAddressDialogController dialogController = loader.getController();
                dialogController.setUserId(userId);

                Stage stage = new Stage();
                stage.setTitle(bundle.getString("Add_new_delivery_address"));
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();


                if (dialogController.validateInput()) {
                    getDeliveryAddresses(); // Refresh the list after saving
                } else {
                    showError(bundle.getString("Invalid_address"));
                }



            } catch (IOException e) {
                e.printStackTrace();
                showError(bundle.getString("Address_dialog_error"));
            }
        });

    }

    public void setRid(int rid) {
        this.rid = rid;
    }
    public void updateView() {
        renderSummary();
    }
    private void openConfirmationDialog() {
        // Create the confirmation dialog
        if (selectedAddress == null) {
            showError(bundle.getString("No_address_selected"));
        } else {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle(bundle.getString("Order_confirmation"));

            // Create the layout for the dialog
            VBox dialogContent = new VBox();
            dialogContent.setSpacing(10);

            SessionManager session = SessionManager.getInstance();
            Restaurant restaurant = session.getCart(rid).getRestaurant();
            Label restaurantLabel = new Label(restaurant.getRestaurantName());
            restaurantLabel.setStyle("-fx-font-weight: bold;");
            restaurantLabel.setStyle("-fx-font-size: 20px;");
            dialogContent.getChildren().add(restaurantLabel);

            // Display cart items
            Label cartItemsLabel = new Label(bundle.getString("Items"));
            cartItemsLabel.setStyle("-fx-font-weight: bold;");
            VBox cartItems = new VBox();

            for (CartItem item : SessionManager.getInstance().getCart(rid).getItems()) {
                HBox itemBox = new HBox();
                itemBox.setSpacing(10);
                Label name = new Label("• " + item.getProduct().getProductName());
                Label quantity = new Label(bundle.getString("Quantity") + item.getQuantity());
                Label price = new Label(bundle.getString("Price") + item.getTotalPrice() + "€");
                itemBox.getChildren().addAll(name, quantity, price);
                cartItems.getChildren().add(itemBox);
            }

            // Display delivery address
            Label deliveryLabel = new Label(bundle.getString("Delivery_Address"));
            Label selectedAddressLabel = new Label(selectedAddress.getStreetAddress() + ", "
                    + selectedAddress.getPostalCode()
                    + " " + selectedAddress.getCity()
                    + " (" + selectedAddress.getInfo() + ")");
            deliveryLabel.setStyle("-fx-font-weight: bold;");

            // Display selected payment method
            Label paymentLabel = new Label(bundle.getString("paymentMethod"));
            paymentLabel.setStyle("-fx-font-weight: bold;");
            Label selectedPaymentLabel = new Label(selectedPaymentMethod);

            // Display subtotal
            Label totalLabel = new Label(bundle.getString("Total") + subTotal.getText());
            totalLabel.setStyle("-fx-font-weight: bold;");

            // Add all the elements to the dialog layout
            dialogContent.getChildren().addAll(cartItemsLabel, cartItems, deliveryLabel,
                    selectedAddressLabel, paymentLabel, selectedPaymentLabel, totalLabel);
            dialog.getDialogPane().setContent(dialogContent);

            // Add confirmation and cancel buttons
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);


            // Show the dialog and wait for response
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Handle order confirmation
                    confirmOrder();
                    System.out.println("Order confirmed");
                } else {
                    // Handle cancel
                    System.out.println("Order cancelled");
                }
            });
        }
    }


    public void renderSummary() {
        SessionManager session = SessionManager.getInstance();
        ShoppingCart cart = session.getCart(rid);
        Restaurant restaurant = session.getCart(rid).getRestaurant();
        Label restaurantLabel = new Label(restaurant.getRestaurantName());
        restaurantLabel.setStyle("-fx-font-weight: bold;");

        summaryContainer.getChildren().add(restaurantLabel);
        for (CartItem item : cart.getItems()) {
            HBox container = new HBox();
            container.setSpacing(5);
            Label name = new Label();
            Label price = new Label();
            Label quantity = new Label();
            name.setText(item.getProduct().getProductName());
            price.setText((item.getProduct().getProductPrice()) + "€");
            quantity.setText("x " + (item.getQuantity()));
            container.getChildren().addAll(name, price, quantity);
            summaryContainer.getChildren().add(container);
        }

        subTotal.setText((cart.getTotalPrice() + "€"));
    }


    private void getDeliveryAddresses() {
        Task<List<DeliveryAddress>> task = checkoutService.fetchDeliveryAddresses();
        System.out.println("Fetching delivery addresses...");
        task.setOnSucceeded(event -> {
            deliveryAddressesList = task.getValue();
            deliveryAddressesList.sort((a, b) -> Boolean.compare(b.isDefaultAddress(), a.isDefaultAddress()));

            Platform.runLater(() -> {
                deliveryAddresses.getItems().clear();
                if (!deliveryAddressesList.isEmpty()) {
                    int index = 1;
                    for (DeliveryAddress address : deliveryAddressesList) {
                        // Numbering the addresses starting from 1
                        String formattedAddress = index++ + ". " +
                                address.getStreetAddress() + ", " +
                                address.getPostalCode() + " " +
                                address.getCity() + " (" + address.getInfo() + ")";
                        deliveryAddresses.getItems().add(formattedAddress);
                        orderBtn.setDisable(false);
                    }

                    deliveryAddresses.getSelectionModel().selectFirst();
                    selectedAddress = deliveryAddressesList.get(0);
                    System.out.println("Delivery addresses fetched successfully.");
                } else {
                    // Do not open the dialog automatically
                    // Instead, prompt the user or disable the order button
                    showError(bundle.getString("No_saved_delivery_addresses"));
                    // Optionally, disable the order button
                    orderBtn.setDisable(true);
                }
            });
        });

        task.setOnFailed(event -> {
            System.out.println(bundle.getString("Delivery_address_fetch_error"));
        });

        // Execute the task in a new thread
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void populatePaymentMethods() {
        paymentMethod.getItems().addAll(bundle.getString("OFOS_Credits"));
        paymentMethod.getItems().addAll(bundle.getString("Card"));
        paymentMethod.getItems().addAll(bundle.getString("Cash"));
        paymentMethod.getSelectionModel().selectFirst();
        selectedPaymentMethod = paymentMethod.getItems().get(0);
    }

    public void confirmOrder() {
        // Get shopping cart items
        List<CartItem> cartItems = SessionManager.getInstance().getCart(rid).getItems();
        int deliveryAddressId = selectedAddress.getDeliveryAddressId();

        Task<Void> task = orderService.confirmOrder(cartItems, deliveryAddressId,rid);

        task.setOnSucceeded(event -> {
            // Show confirmation dialog on success
            Platform.runLater(() -> {
                SessionManager.getInstance().removeCart(rid);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(bundle.getString("Order_confirmation"));
                alert.setHeaderText(null);
                alert.setContentText(bundle.getString("Order_succesful"));
                alert.showAndWait();
                goToMain();

            });
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("Order_failed"));
                alert.setHeaderText(null);
                alert.setContentText(bundle.getString("Order_failed."));
                alert.showAndWait();
            });
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

}
