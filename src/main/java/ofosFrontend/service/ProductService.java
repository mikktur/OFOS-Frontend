package ofosFrontend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ofosFrontend.model.Product;
import ofosFrontend.session.SessionManager;
import okhttp3.*;

import java.io.IOException;
import java.util.List;

public class ProductService {
    private static final String API_URL = "http://localhost:8000/"; //
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // Existing method to get products by restaurant ID
    public List<Product> getProductsByRID(int id) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        Request request = new Request.Builder()
                .url(API_URL + "api/products/restaurant/" + id)
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

    public void updateProduct(Product product) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String productJson = mapper.writeValueAsString(product);  // Serialize the product to JSON
        SessionManager sessionManager = SessionManager.getInstance();
        String bearerToken = sessionManager.getToken();
        System.out.println("Frontti testi");

        System.out.println("Body testi: " + productJson); // Print the JSON string directly

        RequestBody body = RequestBody.create(productJson, JSON);


        // Create the request with the Bearer token in the Authorization header
        Request request = new Request.Builder()
                .url(API_URL + "api/products/update")
                .put(body)
                .addHeader("Authorization", "Bearer " + bearerToken)  // Add the Bearer token
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to update product: " + response);
            }
        }
    }
    public void deleteProduct(Product product) throws IOException {
        SessionManager sessionManager = SessionManager.getInstance();
        String bearerToken = sessionManager.getToken();

        Request request = new Request.Builder()
                .url(API_URL + "api/products/delete/" + product.getProductID())
                .delete()  // DELETE method
                .addHeader("Authorization", "Bearer " + bearerToken)  // Ensure token is correct
                .build();




        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Failed to delete product: " + response);
        }
    }

}
