package ofosFrontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import ofosFrontend.model.*;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class for handling order operations
 */
public class OrderService {
    private static final String API_URL = "http://localhost:8000/api/";
    private final ObjectMapper mapper = new ObjectMapper();
    OkHttpClient client = new OkHttpClient();

    /**
     * Fetches the order history of the currently logged-in user.
     * @return A map of order IDs to lists of OrderHistory objects.
     * @throws IOException If an I/O error occurs.
     */
    public Map<Integer, List<OrderHistory>> getHistory() throws IOException {
        String lang = LocalizationManager.getLanguageCode();
        String url = API_URL + "order/" + lang + "/history";


        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to fetch order history. Status code: " + response.code());
            }

            String responseBody = response.body().string();
            return mapper.readValue(responseBody, new TypeReference<Map<Integer, List<OrderHistory>>>() {});
        } catch (IOException e) {
            throw new IOException("Failed to fetch order history: " + e.getMessage());
        }
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
            protected Void call() throws IOException {
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


                Request request = new Request.Builder()
                        .url(API_URL + "order")
                        .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                        .header("Content-Type", "application/json")
                        .post(RequestBody.create(requestBody, MediaType.get("application/json")))
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Failed to confirm the order. Status code: " + response.code());
                    }
                }

                return null;
            }
        };
    }
}

