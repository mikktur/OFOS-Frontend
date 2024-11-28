package ofosFrontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import ofosFrontend.model.DeliveryAddress;
import ofosFrontend.session.SessionManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.List;

public class CheckoutService {
    private static final String API_URL = "http://localhost:8000/api/";
    private final OkHttpClient client = new OkHttpClient();

    public Task<List<DeliveryAddress>> fetchDeliveryAddresses() {
        int userId = SessionManager.getInstance().getUserId();

        return new Task<>() {
            @Override
            protected List<DeliveryAddress> call() throws Exception {
                String url = API_URL + "deliveryaddress/" + userId;
                String token = SessionManager.getInstance().getToken();

                // Build the GET request
                Request request = new Request.Builder()
                        .url(url)
                        .header("Authorization", "Bearer " + token)
                        .get()
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readValue(
                                responseBody,
                                new TypeReference<List<DeliveryAddress>>() {
                                }
                        );
                    } else {
                        throw new IOException("Failed to fetch delivery addresses. Status code: " + response.code());
                    }
                }
            }
        };
    }
}
