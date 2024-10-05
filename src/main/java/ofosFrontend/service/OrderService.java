package ofosFrontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ofosFrontend.model.Order;
import ofosFrontend.model.OrderHistory;
import ofosFrontend.model.OrderProducts;
import ofosFrontend.model.Product;
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

// method to retrieve orders by user id
// returns a list of objects
public class OrderService {
    private static final String API_URL = "http://localhost:8000/api/";
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    /*
    public List<Order> getOrdersByUserId(int id) throws IOException {
        Request request = new Request.Builder()
                .url(API_URL + "order/" + id)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println("Response Body: " + responseBody);

        List<Order> orders = mapper.readValue(responseBody,
                mapper.getTypeFactory().constructCollectionType(List.class, Order.class));

        return orders;
    }

    public List<OrderProducts> getOrderProductsByUserId(int id) throws IOException {
        Request request = new Request.Builder()
                .url(API_URL + "order/products/" + id)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println("Response Body: " + responseBody);

        List<OrderProducts> orderProducts = mapper.readValue(responseBody,
                mapper.getTypeFactory().constructCollectionType(List.class, OrderProducts.class));

        return orderProducts;
    }
    */

    public Map<Integer, List<OrderHistory>> getHistory() throws IOException, InterruptedException {
        String url = API_URL + "order/history";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        ObjectMapper mapper = new ObjectMapper();
        Map<Integer, List<OrderHistory>> orderHistoryMap = mapper.readValue(responseBody, new TypeReference<Map<Integer, List<OrderHistory>>>(){});

        return orderHistoryMap;
    }
}

