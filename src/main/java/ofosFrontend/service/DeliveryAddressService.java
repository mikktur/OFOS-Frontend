package ofosFrontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import ofosFrontend.model.DeliveryAddress;
import ofosFrontend.session.SessionManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DeliveryAddressService {
    private static final String API_URL = "http://localhost:8000/api/";

    public void saveDeliveryAddress(DeliveryAddress address, Runnable onSuccess, Runnable onFailure) {
        try {
            System.out.println("Saving delivery address...");
            String url = API_URL + "deliveryaddress/save";

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(address);

            System.out.println("Request Body: " + requestBody);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            responseFuture.thenAccept(response -> {
                if (response.statusCode() == 200) {
                    Platform.runLater(onSuccess);  // Execute success logic on the UI thread
                } else {
                    Platform.runLater(() -> onFailure.run());
                }
            }).exceptionally(ex -> {
                ex.printStackTrace();
                Platform.runLater(onFailure);  // Handle failure
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(onFailure);
        }
    }

    public Task<List<DeliveryAddress>> fetchDeliveryAddresses(int userId) {
        return new Task<>() {
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
                    return objectMapper.readValue(responseBody, new TypeReference<List<DeliveryAddress>>() {});
                } else {
                    throw new Exception("Failed to fetch delivery addresses. Status code: " + response.statusCode());
                }
            }
        };
    }

    public Task<Void> setDefaultAddress(DeliveryAddress address, int userId) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                String url = API_URL + "deliveryaddress/setDefault";
                HttpClient client = HttpClient.newHttpClient();
                ObjectMapper objectMapper = new ObjectMapper();


                Map<String, Integer> requestBody = new HashMap<>();
                requestBody.put("deliveryAddressId", address.getDeliveryAddressId());
                requestBody.put("userId", userId);

                String requestBodyJson = objectMapper.writeValueAsString(requestBody);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                        .method("PUT", HttpRequest.BodyPublishers.ofString(requestBodyJson))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new Exception("Failed to set default address. Status code: " + response.statusCode());
                }

                return null;
            }
        };
    }

    public Task<Void> deleteAddress(int deliveryAddressId) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                String url = API_URL + "deliveryaddress/delete/" + deliveryAddressId;

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                        .DELETE()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new Exception("Failed to delete address. Status code: " + response.statusCode());
                }

                return null;
            }
        };
    }

    public Task<Void> updateDeliveryAddress(DeliveryAddress address) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                String url = API_URL + "deliveryaddress/update";
                String token = SessionManager.getInstance().getToken();

                ObjectMapper objectMapper = new ObjectMapper();
                String requestBody = objectMapper.writeValueAsString(address);

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new Exception("Failed to update address. Status code: " + response.statusCode());
                }

                return null;
            }
        };
    }
}
