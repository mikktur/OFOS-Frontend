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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ofosFrontend.controller.User.userSettings.AddAddressDialogController;
import ofosFrontend.model.*;
import ofosFrontend.service.OrderService;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import ofosFrontend.service.CheckoutService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static ofosFrontend.session.GenericHelper.executeTask;
import static ofosFrontend.session.Validations.showError;

/**
 * Controller class for the checkout view.
 * The view is used to confirm the order and select the delivery address and payment method.
 * The user can also add a new delivery address.
 */
public class CheckoutController extends BasicController {
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
    private static final String FONT_WEIGHT_BOLD = "-fx-font-weight: bold;";
    private int rid;
    private final Logger logger = LogManager.getLogger(CheckoutController.class);
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(LocalizationManager.getLocale());
    ResourceBundle bundle = LocalizationManager.getBundle();
    CheckoutService checkoutService = new CheckoutService();

    private List<DeliveryAddress> deliveryAddressesList = new ArrayList<>();
    private DeliveryAddress selectedAddress;
    private String selectedPaymentMethod;
    private final OrderService orderService = new OrderService();

    public CheckoutController() {
        // Required by FXML loader
    }

    /**
     * Initializes the view.
     * Fetches the delivery addresses and populates the payment methods.
     * Sets up the listeners for the choice boxes and buttons.
     */
    @FXML
    public void initialize() {


        getDeliveryAddresses();
        populatePaymentMethods();
        setupListeners();
    }

    /**
     * Sets up the listeners for the choice boxes and buttons.
     * The listener for the delivery addresses updates the selected address.
     * The listener for the payment methods updates the selected payment method.
     * The action for the order button opens the confirmation dialog.
     */
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

                Stage stage = new Stage();
                stage.setTitle(bundle.getString("Add_new_delivery_address"));
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();


                String validationError = dialogController.validateInput();
                if (validationError == null) {
                    getDeliveryAddresses(); // Refresh the list after saving
                } else {
                    showError(validationError); // Show the specific validation error
                }


            } catch (IOException e) {
                logger.error("Failed to open add address dialog", e);
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

    /**
     * Opens the confirmation dialog for the order.
     */
    private void openConfirmationDialog() {
        if (selectedAddress == null) {
            showError(bundle.getString("No_address_selected"));
            return;
        }

        Dialog<ButtonType> dialog = createOrderConfirmationDialog();
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                confirmOrder();
            }
        });
    }

    private Dialog<ButtonType> createOrderConfirmationDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(bundle.getString("Order_confirmation"));

        VBox dialogContent = buildOrderSummary();
        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        return dialog;
    }

    private VBox buildOrderSummary() {
        VBox content = new VBox(10);

        Label restaurantLabel = new Label(SessionManager.getInstance().getCart(rid).getRestaurant().getRestaurantName());
        restaurantLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        content.getChildren().add(restaurantLabel);

        content.getChildren().addAll(
                createCartSummary(),
                createDeliveryAddressSummary(),
                createPaymentMethodSummary(),
                createTotalSummary()
        );

        return content;
    }

    private VBox createCartSummary() {
        VBox cartSummary = new VBox();
        Label cartItemsLabel = new Label(bundle.getString("Items"));
        cartItemsLabel.setStyle(FONT_WEIGHT_BOLD);

        VBox cartItems = new VBox();
        for (CartItem item : SessionManager.getInstance().getCart(rid).getItems()) {
            HBox itemBox = new HBox(10);
            itemBox.getChildren().addAll(
                    new Label("• " + item.getProduct().getProductName()),
                    new Label(bundle.getString("Quantity") + item.getQuantity()),
                    new Label(bundle.getString("Price") + currencyFormatter.format(item.getTotalPrice()))
            );
            cartItems.getChildren().add(itemBox);
        }

        cartSummary.getChildren().addAll(cartItemsLabel, cartItems);
        return cartSummary;
    }

    private VBox createDeliveryAddressSummary() {
        VBox addressSummary = new VBox();
        Label deliveryLabel = new Label(bundle.getString("Delivery_Address"));
        deliveryLabel.setStyle(FONT_WEIGHT_BOLD);

        TextFlow addressFlow = new TextFlow(
                new Text(selectedAddress.getStreetAddress() + ", "),
                new Text(selectedAddress.getPostalCode() + " "),
                new Text(selectedAddress.getCity() + " "),
                new Text("(" + selectedAddress.getInfo() + ")")
        );
        addressFlow.setMaxWidth(350);

        addressSummary.getChildren().addAll(deliveryLabel, addressFlow);
        return addressSummary;
    }

    private VBox createPaymentMethodSummary() {
        VBox paymentSummary = new VBox();
        Label paymentLabel = new Label(bundle.getString("paymentMethod"));
        paymentLabel.setStyle(FONT_WEIGHT_BOLD);
        Label selectedPaymentLabel = new Label(selectedPaymentMethod);

        paymentSummary.getChildren().addAll(paymentLabel, selectedPaymentLabel);
        return paymentSummary;
    }

    private VBox createTotalSummary() {
        VBox totalSummary = new VBox();
        Label totalLabel = new Label(bundle.getString("Total") + subTotal.getText());
        totalLabel.setStyle(FONT_WEIGHT_BOLD);
        totalSummary.getChildren().add(totalLabel);
        return totalSummary;
    }


    /**
     * Renders the summary of the order.
     * Displays the restaurant name, cart items, and subtotal.
     */
    public void renderSummary() {
        SessionManager session = SessionManager.getInstance();
        ShoppingCart cart = session.getCart(rid);
        Restaurant restaurant = session.getCart(rid).getRestaurant();
        Label restaurantLabel = new Label(restaurant.getRestaurantName());
        restaurantLabel.setStyle(FONT_WEIGHT_BOLD);

        summaryContainer.getChildren().add(restaurantLabel);
        for (CartItem item : cart.getItems()) {
            HBox container = new HBox();
            container.setSpacing(5);
            Label name = new Label();
            Label price = new Label();
            Label quantity = new Label();
            name.setText(item.getProduct().getProductName());
            price.setText(currencyFormatter.format(item.getTotalPrice()));
            quantity.setText("x " + (item.getQuantity()));
            container.getChildren().addAll(name, price, quantity);
            summaryContainer.getChildren().add(container);
        }

        subTotal.setText((currencyFormatter.format(cart.getTotalPrice())));
    }

    /**
     * Fetches the delivery addresses for the user.
     * Populates the choice box with the addresses.
     */
    private void getDeliveryAddresses() {
        Task<List<DeliveryAddress>> task = checkoutService.fetchDeliveryAddresses();

        executeTask(
                task,
                this::processFetchedAddresses,
                () -> showError(bundle.getString("Delivery_address_fetch_error"))
        );
    }

    /**
     * Processes the fetched delivery addresses.
     *
     * @param addresses The list of delivery addresses.
     *                  Sorts the addresses by default address first.
     */
    private void processFetchedAddresses(List<DeliveryAddress> addresses) {
        deliveryAddressesList = addresses;
        deliveryAddressesList.sort((a, b) -> Boolean.compare(b.isDefaultAddress(), a.isDefaultAddress()));
        updateDeliveryAddressesUI();
    }

    /**
     * Updates the delivery addresses in the choice box.
     */
    private void updateDeliveryAddressesUI() {
        Platform.runLater(() -> {
            deliveryAddresses.getItems().clear();

            if (!deliveryAddressesList.isEmpty()) {
                int index = 1;
                for (DeliveryAddress address : deliveryAddressesList) {
                    String formattedAddress = formatDeliveryAddress(address, index++);
                    deliveryAddresses.getItems().add(formattedAddress);
                }
                deliveryAddresses.getSelectionModel().selectFirst();
                selectedAddress = deliveryAddressesList.get(0);
                orderBtn.setDisable(false);
            } else {
                showError(bundle.getString("No_saved_delivery_addresses"));
                orderBtn.setDisable(true);
            }
        });
    }

    /**
     * Formats the delivery address for display in the choice box.
     *
     * @param address The delivery address.
     * @param index   The index of the address in the list.
     * @return The formatted address string.
     */
    private String formatDeliveryAddress(DeliveryAddress address, int index) {
        return index + ". " +
                address.getStreetAddress() + ", " +
                address.getPostalCode() + " " +
                address.getCity() + " (" + address.getInfo() + ")";
    }


    /**
     * Populates the payment methods in the choice box.
     */
    private void populatePaymentMethods() {
        List<String> paymentOptions = List.of(
                bundle.getString("OFOS_Credits"),
                bundle.getString("Card"),
                bundle.getString("Cash")
        );

        paymentMethod.getItems().setAll(paymentOptions);
        paymentMethod.getSelectionModel().selectFirst();
        selectedPaymentMethod = paymentMethod.getSelectionModel().getSelectedItem();
    }


    /**
     * Confirms the order and saves it to the database.
     * Shows a confirmation dialog on success.
     * Shows an error dialog on failure.
     */
    public void confirmOrder() {
        // Get shopping cart items
        List<CartItem> cartItems = SessionManager.getInstance().getCart(rid).getItems();
        int deliveryAddressId = selectedAddress.getDeliveryAddressId();

        Task<Void> task = orderService.confirmOrder(cartItems, deliveryAddressId, rid);

        task.setOnSucceeded(event ->
                Platform.runLater(() -> {
                    SessionManager.getInstance().removeCart(rid);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(bundle.getString("Order_confirmation"));
                    alert.setHeaderText(null);
                    alert.setContentText(bundle.getString("Order_succesful"));
                    alert.showAndWait();
                    goToMain();
                })
        );

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            logger.error("Failed to confirm order", exception);
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("Order_failed"));
                alert.setHeaderText(null);
                alert.setContentText(bundle.getString("Order_failed"));
                alert.showAndWait();
            });
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

}
