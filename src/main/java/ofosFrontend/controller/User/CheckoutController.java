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
import ofosFrontend.model.CartItem;
import ofosFrontend.model.DeliveryAddress;
import ofosFrontend.model.ShoppingCart;
import ofosFrontend.service.DeliveryAddressService;
import ofosFrontend.service.OrderService;
import ofosFrontend.session.SessionManager;
import javafx.scene.control.Label;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import ofosFrontend.model.OrderItem;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import ofosFrontend.service.CheckoutService;


public class CheckoutController {
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
        renderSummary();
        getDeliveryAddresses();
        populatePaymentMethods();

        // Add a listener to the deliveryAddresses ChoiceBox
        deliveryAddresses.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 0) {
                selectedAddress = deliveryAddressesList.get(newValue.intValue());
            }
        });

        // Add a listener to the paymentMethod ChoiceBox
        paymentMethod.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 0) {
                selectedPaymentMethod = paymentMethod.getItems().get(newValue.intValue());
            }
        });

        orderBtn.setOnAction(event -> openConfirmationDialog());

    }

    private void openConfirmationDialog() {
        // Create the confirmation dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Confirm Order");

        // Create the layout for the dialog
        VBox dialogContent = new VBox();
        dialogContent.setSpacing(10);

        // Display cart items
        Label cartItemsLabel = new Label("Cart Items:");
        cartItemsLabel.setStyle("-fx-font-weight: bold;");
        VBox cartItems = new VBox();

        for (CartItem item : SessionManager.getInstance().getCart().getItems()) {
            HBox itemBox = new HBox();
            itemBox.setSpacing(10);
            Label name = new Label("• " + item.getProduct().getProductName());
            Label quantity = new Label("Quantity: " + item.getQuantity());
            Label price = new Label("Price: " + item.getTotalPrice());
            itemBox.getChildren().addAll(name, quantity, price);
            cartItems.getChildren().add(itemBox);
        }

        // Display delivery address
        Label deliveryLabel = new Label("Delivery Address:");
        Label selectedAddressLabel = new Label(selectedAddress.getStreetAddress() + ", "
                + selectedAddress.getPostalCode()
                + " " + selectedAddress.getCity()
                + " (" + selectedAddress.getInfo() + ")");
        deliveryLabel.setStyle("-fx-font-weight: bold;");

        // Display selected payment method
        Label paymentLabel = new Label("Payment Method:");
        paymentLabel.setStyle("-fx-font-weight: bold;");
        Label selectedPaymentLabel = new Label(selectedPaymentMethod);

        // Display subtotal
        Label totalLabel = new Label("Total: " + subTotal.getText());
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


    public void renderSummary() {
        SessionManager session = SessionManager.getInstance();
        ShoppingCart cart = session.getCart();
        for (CartItem item : cart.getItems()) {
            HBox container = new HBox();
            container.setSpacing(5);
            Label name = new Label();
            Label price = new Label();
            Label quantity = new Label();
            name.setText(item.getProduct().getProductName());
            price.setText(String.valueOf(item.getTotalPrice()));
            quantity.setText(String.valueOf(item.getQuantity()));
            container.getChildren().addAll(name, price, quantity);
            summaryContainer.getChildren().add(container);
        }
        subTotal.setText(String.valueOf(cart.getTotalPrice()));
    }


    private void getDeliveryAddresses() {
        Task<List<DeliveryAddress>> task = checkoutService.fetchDeliveryAddresses();

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
                    }

                    deliveryAddresses.getSelectionModel().selectFirst();
                    selectedAddress = deliveryAddressesList.get(0);
                } else {
                    // Open Add Address Dialog when no addresses are available
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ofosFrontend/addAddressDialog.fxml"));
                        Parent root = loader.load();

                        AddAddressDialogController dialogController = loader.getController();
                        dialogController.setUserId(userId);

                        Stage stage = new Stage();
                        stage.setTitle("Add New Address");
                        stage.setScene(new Scene(root));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();

                        DeliveryAddress newAddress = dialogController.getNewAddress();

                        // Validate input before saving
                        if (dialogController.validateInput()) {
                            deliveryAddressService.saveDeliveryAddress(newAddress, () -> {
                                getDeliveryAddresses();
                            }, () -> {
                                showError("Failed to save address.");
                            });
                        } else {
                            showError("Invalid address input. Please fill all fields.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        showError("An error occurred while opening the add address dialog.");
                    }
                }
            });
        });

        task.setOnFailed(event -> {
            System.out.println("Failed to fetch delivery addresses.");
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
        paymentMethod.getItems().addAll("OFOS Credits","Card", "Cash");
        paymentMethod.getSelectionModel().selectFirst();
        selectedPaymentMethod = paymentMethod.getItems().get(0);
    }

    public void confirmOrder() {
        // Get shopping cart items
        List<CartItem> cartItems = SessionManager.getInstance().getCart().getItems();
        int deliveryAddressId = selectedAddress.getDeliveryAddressId();
        int restaurantId = 2;  // Hardcoded restaurantID

        Task<Void> task = orderService.confirmOrder(cartItems, deliveryAddressId, restaurantId);

        task.setOnSucceeded(event -> {
            // Show confirmation dialog on success
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Order Confirmed");
                alert.setHeaderText(null);
                alert.setContentText("Your order has been successfully placed!");
                alert.showAndWait();
            });
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Order Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to place the order.");
                alert.showAndWait();
            });
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

}
