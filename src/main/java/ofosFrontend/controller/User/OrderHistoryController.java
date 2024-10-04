package ofosFrontend.controller.User;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import ofosFrontend.model.Order;
import ofosFrontend.model.OrderProducts;
import ofosFrontend.service.OrderService;
import ofosFrontend.session.SessionManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class OrderHistoryController {

    OrderService orderService = new OrderService();
    int userId = SessionManager.getInstance().getUserId();

    @FXML
    public GridPane dynamicGridPane;

    @FXML
    public void initialize() {
        System.out.println("User id: " + userId);
        System.out.println("Order History Controller Initialized");
        getOrders(userId);
        getProducts(userId);

    }

    public void getOrders(int id) {
        try {
            List<Order> orders = orderService.getOrdersByUserId(userId);

            for (Order order : orders) {
                System.out.println("Order ID: " + order.getOrderId());
                System.out.println("address " + order.getOrderAddress());
                System.out.println("Date: " + order.getOrderDate());
                System.out.println("Rid: " + order.getRestaurantId());
                System.out.println("State: " + order.getState());
                System.out.println("User ID: " + order.getUserId());

                System.out.println("-----");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getProducts (int id) {
        try {
            List<OrderProducts> products = orderService.getOrderProductsByOrderId(id);

            for (OrderProducts product : products) {
                System.out.println("Product ID: " + product.getProductId());
                System.out.println("Order ID: " + product.getOrderId());
                System.out.println("Quantity: " + product.getQuantity());
                System.out.println("-----");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
