package ofosFrontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import ofosFrontend.model.DeliveryAddress;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import static ofosFrontend.session.Validations.showError;

/**
 * Service class for handling checkout operations
 */
public class CheckoutService {
    private final OkHttpClient client = new OkHttpClient();
    private ResourceBundle bundle = LocalizationManager.getBundle();
    private static final String API_URL = "http://10.120.32.94:8000/api/";

    /**
     * Fetches the delivery addresses of the currently logged-in user asynchronously.
     * @return A Task that fetches the delivery addresses.
     */
    public Task<List<DeliveryAddress>> fetchDeliveryAddresses() {
        int userId = SessionManager.getInstance().getUserId();

        Task<List<DeliveryAddress>> task = new Task<>() {
            @Override
            protected List<DeliveryAddress> call() throws Exception {
                String url = API_URL + "deliveryaddress/" + userId;
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    String responseBody = response.body();
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(
                            responseBody,
                            new TypeReference<List<DeliveryAddress>>() {}
                    );
                } else {
                    throw new Exception("Failed to fetch delivery addresses. Status code: " + response.statusCode());
                }
            }
        };


        task.setOnSucceeded(event -> {
            deliveryAddressesList = task.getValue();
        });

        task.setOnFailed(event -> {
            Throwable e = task.getException();
            e.printStackTrace();
            showError(bundle.getString("Delivery_address_fetch_error"));
        });

        return task;
    }

    public List<DeliveryAddress> getDeliveryAddressesList() {
        return deliveryAddressesList;
    }
}
