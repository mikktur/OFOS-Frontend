package ofosFrontend.controller.User;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.geometry.HPos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ofosFrontend.model.OrderHistory;
import ofosFrontend.service.OrderHistorySorter;
import ofosFrontend.service.OrderService;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;

import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static ofosFrontend.session.Validations.showError;

/**
 * Controller for the order history view.
 */
public class OrderHistoryController {
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(LocalizationManager.getLocale());
    private final OrderService orderService = new OrderService();
    private final int userId = SessionManager.getInstance().getUserId();
    private final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, LocalizationManager.getLocale());
    ResourceBundle bundle = LocalizationManager.getBundle();

    @FXML
    private GridPane historyGridPane;

    @FXML
    public void initialize() {
        if (historyGridPane.getColumnConstraints().isEmpty()) {
            historyGridPane.getColumnConstraints().addAll(
                    createColumnConstraint(12),  // Order ID
                    createColumnConstraint(23), // Restaurant Name
                    createColumnConstraint(25), // Products
                    createColumnConstraint(15), // Total Price
                    createColumnConstraint(25)  // Order Date
            );
        }
        loadOrderHistory();
    }

    /**
     * Creates a column constraint with a percentage width.
     *
     * @param percentWidth The percentage width of the column.
     * @return A ColumnConstraints object.
     */
    private ColumnConstraints createColumnConstraint(double percentWidth) {
        ColumnConstraints constraint = new ColumnConstraints();
        constraint.setPercentWidth(percentWidth);
        return constraint;
    }


    /**
     * Loads and displays the user's order history.
     * Sorts the order history by order ID.
     */
    public void loadOrderHistory() {
        try {
            Map<Integer, List<OrderHistory>> orderHistoryMap = orderService.getHistory();
            orderHistoryMap = OrderHistorySorter.sortOrderHistoryById(orderHistoryMap);

            displayOrderHistory(orderHistoryMap);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            showError(bundle.getString("Order_fetch_error"));
        }
    }

    /**
     * Displays the sorted order history in the GridPane.
     *
     * @param orderHistoryMap A map of order IDs to their corresponding order history.
     */
    private void displayOrderHistory(Map<Integer, List<OrderHistory>> orderHistoryMap) {
        historyGridPane.getChildren().clear();
        int rowIndex = 0; // Tracks the physical rows in the GridPane
        int logicalRowIndex = 0; // Tracks only the data rows

        for (Map.Entry<Integer, List<OrderHistory>> entry : orderHistoryMap.entrySet()) {
            Integer orderId = entry.getKey();
            List<OrderHistory> orderItems = entry.getValue();

            // Alternate row colors based on logical index
            String backgroundColor = (logicalRowIndex % 2 == 0) ? "white" : "#c4e6fb";

            addOrderRow(orderId, orderItems, rowIndex, backgroundColor);

            // Add separator after each row
            addRowSeparator(++rowIndex);

            // Increment both indices
            rowIndex++;
            logicalRowIndex++;
        }
    }


    /**
     * Adds a single order row to the GridPane.
     *
     * @param orderId    The ID of the order.
     * @param orderItems The list of products in the order.
     * @param rowIndex   The row index in the GridPane.
     */
    private void addOrderRow(Integer orderId, List<OrderHistory> orderItems, int rowIndex, String backgroundColor) {
        // Create a VBox to act as the row container
        VBox rowContainer = new VBox();
        rowContainer.setStyle("-fx-background-color: " + backgroundColor + ";");
        rowContainer.setPrefHeight(30);
        rowContainer.setFillWidth(true);

        // Create the content for the row
        GridPane rowContent = new GridPane();
        rowContent.setPrefWidth(historyGridPane.getWidth());
        rowContent.getColumnConstraints().addAll(historyGridPane.getColumnConstraints());
        rowContent.setStyle("-fx-background-color: transparent;");

        // Order ID
        Label orderIdLabel = createCenteredLabel(orderId.toString());
        rowContent.add(orderIdLabel, 0, 0);
        GridPane.setHalignment(orderIdLabel, HPos.CENTER);

        // Restaurant Name
        Label restaurantLabel = createCenteredLabel(orderItems.get(0).getRestaurantName());
        rowContent.add(restaurantLabel, 1, 0);
        GridPane.setHalignment(restaurantLabel, HPos.CENTER);

        // Products ComboBox
        ComboBox<OrderHistory> productsComboBox = createProductsComboBox(orderItems, backgroundColor);
        rowContent.add(productsComboBox, 2, 0);
        GridPane.setHalignment(productsComboBox, HPos.CENTER);

        // Total Price
        Label totalPriceLabel = createCenteredLabel(formatTotalPrice(orderItems));
        rowContent.add(totalPriceLabel, 3, 0);
        GridPane.setHalignment(totalPriceLabel, HPos.CENTER);

        // Order Date
        Label orderDateLabel = createCenteredLabel(formatOrderDate(orderItems.get(0).getOrderDate()));
        rowContent.add(orderDateLabel, 4, 0);
        GridPane.setHalignment(orderDateLabel, HPos.CENTER);

        // Add the rowContent to the rowContainer
        rowContainer.getChildren().add(rowContent);

        // Add the rowContainer to the GridPane
        historyGridPane.add(rowContainer, 0, rowIndex, 5, 1);
    }


    /**
     * Creates a centered label with the given text.
     *
     * @param text The text to display.
     * @return A centered label.
     */
    private Label createCenteredLabel(String text) {
        Label label = new Label(text);
        GridPane.setHalignment(label, HPos.CENTER);
        return label;
    }

    /**
     * Creates a ComboBox with the given order items and background color.
     * @param orderItems The list of products in the order.
     * @param backgroundColor The background color for the ComboBox.
     * @return A ComboBox with the order items.
     */
    private ComboBox<OrderHistory> createProductsComboBox(List<OrderHistory> orderItems, String backgroundColor) {
        ComboBox<OrderHistory> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(300);
        comboBox.setPrefHeight(30);

        // Add OrderHistory objects directly to the ComboBox
        comboBox.getItems().addAll(orderItems);
        if (!orderItems.isEmpty()) {
            comboBox.setValue(orderItems.getFirst());
        }

        applyCustomCellFactory(comboBox, backgroundColor);

        return comboBox;
    }

    /**
     * Applies a custom cell factory to the ComboBox.
     *
     * @param comboBox       The ComboBox to apply the custom cell factory to.
     * @param backgroundColor The background color for the ComboBox.
     */
    private void applyCustomCellFactory(ComboBox<OrderHistory> comboBox, String backgroundColor) {
        // Custom cell factory for dropdown items
        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(OrderHistory item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(createOrderHistoryGrid(item));
                }
            }
        });

        // Custom button cell to display the summary
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(OrderHistory item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || comboBox.getItems().isEmpty()) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Calculate the total quantity and total price
                    int totalQuantity = comboBox.getItems().stream()
                            .mapToInt(OrderHistory::getQuantity)
                            .sum();
                    double totalPrice = comboBox.getItems().stream()
                            .mapToDouble(o -> o.getOrderPrice() * o.getQuantity())
                            .sum();

                    // Fetch localized strings
                    String itemLabel = (totalQuantity > 1)
                            ? bundle.getString("items") // Plural form
                            : bundle.getString("item"); // Singular form
                    String totalSumLabel = bundle.getString("total_sum");

                    // Create a localized summary
                    String summary = String.format("%d %s, %s: %s",
                            totalQuantity, itemLabel, totalSumLabel, currencyFormatter.format(totalPrice));

                    Label summaryLabel = new Label(summary);
                    summaryLabel.setStyle("-fx-text-fill: black;");

                    setGraphic(summaryLabel);
                    setStyle("-fx-background-color: " + backgroundColor + ";");
                }
            }
        });

        // Overall ComboBox styling
        comboBox.setStyle("-fx-background-color: " + backgroundColor + "; -fx-border-color: transparent;");
    }



    /**
     * Creates a GridPane layout for displaying an OrderHistory item.
     *
     * @param item The OrderHistory item to display.
     * @return A GridPane containing the item's details.
     */
    private GridPane createOrderHistoryGrid(OrderHistory item) {
        GridPane grid = new GridPane();
        grid.setHgap(10);

        // Product Name
        Label nameLabel = new Label(item.getProductName());
        nameLabel.setStyle("-fx-text-fill: black;");
        GridPane.setConstraints(nameLabel, 0, 0);
        grid.getChildren().add(nameLabel);

        // Quantity
        Label quantityLabel = new Label("(x" + item.getQuantity() + ")");
        quantityLabel.setStyle("-fx-text-fill: black;");
        GridPane.setConstraints(quantityLabel, 1, 0);
        grid.getChildren().add(quantityLabel);

        // Price
        Label priceLabel = new Label(currencyFormatter.format(item.getOrderPrice()));
        priceLabel.setStyle("-fx-text-fill: black;");
        GridPane.setConstraints(priceLabel, 2, 0);
        grid.getChildren().add(priceLabel);

        // Apply grid column percentages for proper alignment
        grid.getColumnConstraints().addAll(
                createColumnConstraint(65),
                createColumnConstraint(10),
                createColumnConstraint(25)
        );

        return grid;
    }


    /**
     * Formats the total price for the order.
     *
     * @param orderItems The list of products in the order.
     * @return A formatted total price string.
     */
    private String formatTotalPrice(List<OrderHistory> orderItems) {
        double totalPrice = orderItems.stream()
                .mapToDouble(item -> item.getOrderPrice() * item.getQuantity())
                .sum();
        return currencyFormatter.format(totalPrice);
    }

    /**
     * Formats the order date.
     *
     * @param rawOrderDate The raw order date string.
     * @return A formatted date string.
     */
    private String formatOrderDate(String rawOrderDate) {
        try {
            Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(rawOrderDate);
            return dateFormatter.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid date";
        }
    }

    /**
     * Adds a horizontal line or spacer between rows in the GridPane.
     *
     * @param rowIndex The row index to add the separator.
     */
    private void addRowSeparator(int rowIndex) {
        HBox separator = new HBox();
        separator.setStyle("-fx-background-color: lightgray; -fx-min-height: 1px;");
        separator.setPrefHeight(1);

        historyGridPane.add(separator, 0, rowIndex, 5, 1);
    }


}
