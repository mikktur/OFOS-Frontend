package ofosFrontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import ofosFrontend.model.DeliveryAddress;
import ofosFrontend.session.SessionManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class CheckoutService {
    private List<DeliveryAddress> deliveryAddressesList = new ArrayList<>();

    public Task<List<DeliveryAddress>> fetchDeliveryAddresses() {
        int userId = SessionManager.getInstance().getUserId();

        Task<List<DeliveryAddress>> task = new Task<>() {
            @Override
            protected List<DeliveryAddress> call() throws Exception {
                String url = "http://10.120.32.94:8000/api/deliveryaddress/" + userId;
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while retrieving delivery addresses.");
            alert.showAndWait();
        });

        return task;
    }

    public List<DeliveryAddress> getDeliveryAddressesList() {
        return deliveryAddressesList;
    }
}
