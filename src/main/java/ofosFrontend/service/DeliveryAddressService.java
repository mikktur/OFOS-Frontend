package ofosFrontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import ofosFrontend.model.DeliveryAddress;
import ofosFrontend.session.SessionManager;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryAddressService {
    private static final String API_URL = "http://localhost:8000/api/";
    private final ObjectMapper mapper = new ObjectMapper();
    private final OkHttpClient client = new OkHttpClient();
    private final Logger logger = LogManager.getLogger(DeliveryAddressService.class);
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_JSON = "application/json";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    public void saveDeliveryAddress(DeliveryAddress address, Runnable onSuccess, Runnable onFailure) {
        try {
            logger.info("Saving delivery address...");
            String url = API_URL + "deliveryaddress/save";

            String requestBody = mapper.writeValueAsString(address);
            logger.info("Request Body: {}", requestBody);

            Request request = new Request.Builder()
                    .url(url)
                    .header(CONTENT_TYPE, CONTENT_JSON)
                    .header(AUTHORIZATION, BEARER_PREFIX + SessionManager.getInstance().getToken())
                    .post(RequestBody.create(requestBody, MediaType.get(CONTENT_JSON)))
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    logger.error("Failed to save delivery address", e);
                    Platform.runLater(onFailure); // Handle failure
                }

                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        Platform.runLater(onSuccess); // Execute success logic on the UI thread
                    } else {
                        Platform.runLater(onFailure);
                    }
                }
            });
        } catch (Exception e) {
            logger.error("Failed to save delivery address", e);
            Platform.runLater(onFailure);
        }
    }

    public Task<List<DeliveryAddress>> fetchDeliveryAddresses(int userId) {
        return new Task<>() {
            @Override
            protected List<DeliveryAddress> call() throws IOException {
                String url = API_URL + "deliveryaddress/" + userId;

                Request request = new Request.Builder()
                        .url(url)
                        .header(AUTHORIZATION, BEARER_PREFIX + SessionManager.getInstance().getToken())
                        .get()
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        return mapper.readValue(responseBody, new TypeReference<List<DeliveryAddress>>() {
                        });
                    } else {
                        throw new IOException("Failed to fetch delivery addresses. Status code: " + response.code());
                    }
                }
            }
        };
    }

    public Task<Void> setDefaultAddress(DeliveryAddress address, int userId) {
        return new Task<>() {
            @Override
            protected Void call() throws IOException {
                String url = API_URL + "deliveryaddress/setDefault";

                Map<String, Integer> requestBody = new HashMap<>();
                requestBody.put("deliveryAddressId", address.getDeliveryAddressId());
                requestBody.put("userId", userId);

                String requestBodyJson = mapper.writeValueAsString(requestBody);

                Request request = new Request.Builder()
                        .url(url)
                        .header(CONTENT_TYPE, CONTENT_JSON)
                        .header(AUTHORIZATION, BEARER_PREFIX + SessionManager.getInstance().getToken())
                        .put(RequestBody.create(requestBodyJson, MediaType.get(CONTENT_JSON)))
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Failed to set default address. Status code: " + response.code());
                    }
                }

                return null;
            }
        };
    }

    public Task<Void> deleteAddress(int deliveryAddressId) {
        return new Task<>() {
            @Override
            protected Void call() throws IOException {
                String url = API_URL + "deliveryaddress/delete/" + deliveryAddressId;

                Request request = new Request.Builder()
                        .url(url)
                        .header(AUTHORIZATION, BEARER_PREFIX + SessionManager.getInstance().getToken())
                        .delete()
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Failed to delete address. Status code: " + response.code());
                    }
                }

                return null;
            }
        };
    }

    public Task<Void> updateDeliveryAddress(DeliveryAddress address) {
        return new Task<>() {
            @Override
            protected Void call() throws IOException {
                String url = API_URL + "deliveryaddress/update";

                String requestBody = mapper.writeValueAsString(address);

                Request request = new Request.Builder()
                        .url(url)
                        .header(CONTENT_TYPE, CONTENT_JSON)
                        .header(AUTHORIZATION, BEARER_PREFIX + SessionManager.getInstance().getToken())
                        .put(RequestBody.create(requestBody, MediaType.get(CONTENT_JSON)))
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Failed to update address. Status code: " + response.code());
                    }
                }

                return null;
            }
        };
    }
}
