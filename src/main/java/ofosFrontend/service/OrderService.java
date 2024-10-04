package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ofosFrontend.model.Order;
import ofosFrontend.model.OrderProducts;
import ofosFrontend.model.Product;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

// method to retrieve orders by user id
// returns a list of objects
public class OrderService {
    private static final String API_URL = "http://localhost:8000/api/order/";
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Order> getOrdersByUserId(int id) throws IOException {
        Request request = new Request.Builder()
                .url(API_URL + id)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println("Response Body: " + responseBody);

        List<Order> orders = mapper.readValue(responseBody,
                mapper.getTypeFactory().constructCollectionType(List.class, Order.class));

        return orders;
    }

    public List<OrderProducts> getOrderProductsByOrderId(int id) throws IOException {
        Request request = new Request.Builder()
                .url(API_URL + "products/" + id)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println("Response Body: " + responseBody);

        List<OrderProducts> orderProducts = mapper.readValue(responseBody,
                mapper.getTypeFactory().constructCollectionType(List.class, OrderProducts.class));

        return orderProducts;
    }
}

