package ofosFrontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import ofosFrontend.model.*;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class for handling order operations
 */
public class OrderService {
    private static final String API_URL = "http://10.120.32.94:8000/api/";
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Fetches the order history of the currently logged-in user.
     * @return A map of order IDs to lists of OrderHistory objects.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public Map<Integer, List<OrderHistory>> getHistory() throws IOException, InterruptedException {
        String lang = LocalizationManager.getLanguageCode();
        String url = API_URL + "order/"+lang+"/history";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        Map<Integer, List<OrderHistory>> orderHistoryMap = mapper.readValue(responseBody, new TypeReference<Map<Integer, List<OrderHistory>>>(){});

        return orderHistoryMap;
    }

    /**
     * Confirms an order with the given cart items, delivery address ID, and restaurant ID.
     *
     * @param cartItems The cart items to confirm.
     * @param deliveryAddressId The ID of the delivery address.
     * @param restaurantId The ID of the restaurant.
     * @return A Task that confirms the order.
     */
    public Task<Void> confirmOrder(List<CartItem> cartItems, int deliveryAddressId, int restaurantId) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                List<OrderItem> orderItems = new ArrayList<>();
                for (CartItem item : cartItems) {
                    OrderItem orderItem = new OrderItem(
                            item.getQuantity(),
                            item.getProduct().getProductID(),
                            deliveryAddressId,
                            restaurantId
                    );
                    orderItems.add(orderItem);
                }

                String requestBody = mapper.writeValueAsString(orderItems);

                String url = API_URL + "order";
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new Exception("Failed to confirm the order. Status code: " + response.statusCode());
                }

                return null;
            }
        };
    }
}

