package ofosFrontend.controller.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.HPos;
import ofosFrontend.model.OrderHistory;
import ofosFrontend.service.OrderHistorySorter;
import ofosFrontend.service.OrderService;
import ofosFrontend.session.SessionManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class OrderHistoryController {

    OrderService orderService = new OrderService();
    int userId = SessionManager.getInstance().getUserId();


    @FXML
    private GridPane historyGridPane;

    @FXML
    public void initialize() {
        System.out.println("User id: " + userId);
        System.out.println("Order History Controller Initialized");
        loadOrderHistory();
    }


    public void loadOrderHistory() {
        try {
            // Get the order history for the user
            Map<Integer, List<OrderHistory>> orderHistoryMap = orderService.getHistory();

            // Sort the order history map and maintain the sorted order
            orderHistoryMap = OrderHistorySorter.sortOrderHistoryById(orderHistoryMap);

            int rowIndex = 0;

            for (Map.Entry<Integer, List<OrderHistory>> entry : orderHistoryMap.entrySet()) {
                Integer orderId = entry.getKey();
                List<OrderHistory> orderItems = entry.getValue();

                VBox productsBox = new VBox();
                productsBox.setAlignment(Pos.CENTER);

                double totalPrice = 0;

                // Iterate through the products in the order
                for (OrderHistory item : orderItems) {
                    Label productLabel = new Label(item.getProductName() + " (x" + item.getQuantity() + ") " + item.getOrderPrice() + "€");
                    productsBox.getChildren().add(productLabel);

                    totalPrice += item.getOrderPrice() * item.getQuantity();
                }

                Label orderIdLabel = new Label(orderId.toString());
                historyGridPane.add(orderIdLabel, 0, rowIndex);
                GridPane.setHalignment(orderIdLabel, HPos.CENTER);

                Label restaurantLabel = new Label(orderItems.get(0).getRestaurantName());
                historyGridPane.add(restaurantLabel, 1, rowIndex);
                GridPane.setHalignment(restaurantLabel, HPos.CENTER);

                historyGridPane.add(productsBox, 2, rowIndex);
                GridPane.setHalignment(productsBox, HPos.CENTER);

                Label totalPriceLabel = new Label(String.format("%.2f", totalPrice) + " €");
                historyGridPane.add(totalPriceLabel, 3, rowIndex);
                GridPane.setHalignment(totalPriceLabel, HPos.CENTER);

                Label orderDateLabel = new Label(orderItems.get(0).getOrderDate());
                historyGridPane.add(orderDateLabel, 4, rowIndex);
                GridPane.setHalignment(orderDateLabel, HPos.CENTER);

                // Move to the next row in the grid pane
                rowIndex++;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }



}