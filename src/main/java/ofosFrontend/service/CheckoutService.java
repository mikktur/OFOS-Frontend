package ofosFrontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import ofosFrontend.model.DeliveryAddress;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static ofosFrontend.session.Validations.showError;

/**
 * Service class for handling checkout operations
 */
public class CheckoutService {
    ResourceBundle bundle = LocalizationManager.getBundle();
    private static final String API_URL = "http://10.120.32.94:8000/api/";

    /**
     * Fetches the delivery addresses of the currently logged-in user asynchronously.
     * @return A Task that fetches the delivery addresses.
     */
    public Task<List<DeliveryAddress>> fetchDeliveryAddresses() {
        int userId = SessionManager.getInstance().getUserId();
        String token = SessionManager.getInstance().getToken();
        String url = API_URL + "deliveryaddress/" + userId;

        Task<List<DeliveryAddress>> task = new Task<>() {
            @Override
            protected List<DeliveryAddress> call() throws Exception {
                return fetchDeliveryAddressesFromApi(url, token);
            }
        };

        task.setOnFailed(event -> {
            Throwable e = task.getException();
            e.printStackTrace();
            showError(bundle.getString("Delivery_address_fetch_error"));
        });

        return task;
    }

    /**
     * Makes the HTTP request to fetch delivery addresses from the API.
     * @param url The API endpoint.
     * @param token The authentication token.
     * @return A list of DeliveryAddress objects.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    private List<DeliveryAddress> fetchDeliveryAddressesFromApi(String url, String token) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(
                    response.body(),
                    new TypeReference<List<DeliveryAddress>>() {}
            );
        } else {
            throw new IOException("Failed to fetch delivery addresses. Status code: " + response.statusCode());
        }
    }

}
