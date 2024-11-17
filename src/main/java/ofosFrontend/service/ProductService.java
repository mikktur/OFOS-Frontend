package ofosFrontend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ofosFrontend.model.Product;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;
import okhttp3.*;

import java.io.IOException;
import java.util.List;

public class ProductService {
    private static final String API_URL = "http://localhost:8000/"; //
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Product> getProductsByRID(int id) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        // Get the current language code dynamically
        String language = LocalizationManager.getLanguageCode();

        Request request = new Request.Builder()
                .url(API_URL + "api/products/restaurant/" + language + "/" + id)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        return mapper.readValue(responseBody, mapper.getTypeFactory().constructCollectionType(List.class, Product.class));
    }


    public void addProductToRestaurant(Product product, int restaurantId) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String json = mapper.writeValueAsString(product);

        SessionManager sessionManager = SessionManager.getInstance();
        String bearerToken = sessionManager.getToken();

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(API_URL + "api/products/create/" + restaurantId)
                .post(body)
                .addHeader("Authorization", "Bearer " + bearerToken)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Failed to add product: " + response);
        }
    }

    public void updateProduct(Product product,int rid) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String productJson = mapper.writeValueAsString(product);
        SessionManager sessionManager = SessionManager.getInstance();
        String bearerToken = sessionManager.getToken();
        System.out.println("Frontti testi");

        System.out.println("Body testi: " + productJson);

        RequestBody body = RequestBody.create(productJson, JSON);

        Request request = new Request.Builder()
                .url(API_URL + "api/products/update/"+rid)
                .put(body)
                .addHeader("Authorization", "Bearer " + bearerToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to update product: " + response);
            }
        }
    }

    public void deleteProduct(Product product,int id) throws IOException {
        SessionManager sessionManager = SessionManager.getInstance();
        String bearerToken = sessionManager.getToken();
        System.out.println("testiiii");
        Request request = new Request.Builder()
                .url(API_URL + "api/products/delete/"+product.getProductID()+"/restaurant/" + id)
                .delete()
                .addHeader("Authorization", "Bearer " + bearerToken)
                .build();

        try (Response response = client.newCall(request).execute()) {


            if (!response.isSuccessful()) {
                throw new IOException("Failed to delete product: " + response);
            }
        }

    }
}
