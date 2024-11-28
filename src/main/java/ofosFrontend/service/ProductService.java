package ofosFrontend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ofosFrontend.model.Product;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;
import okhttp3.*;

import java.io.IOException;
import java.util.List;

/**
 * Service class for handling product operations
 */
public class ProductService {
    private static final String API_URL = "http://10.120.32.94:8000/"; //
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Fetches all products of a selected restaurant.
     * @param id The ID of the restaurant.
     * @return A list of Product objects.
     * @throws IOException If an I/O error occurs.
     */
    public List<Product> getProductsByRID(int id) throws IOException {
        String language = LocalizationManager.getLanguageCode();

        Request request = new Request.Builder()
                .url(API_URL + "api/products/restaurant/" + language + "/" + id)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        return mapper.readValue(responseBody, mapper.getTypeFactory().constructCollectionType(List.class, Product.class));
    }

    /**
     * Adds a product to a restaurant.
     * @param product The product to add.
     * @param restaurantId The ID of the restaurant.
     * @throws IOException If an I/O error occurs.
     *
     */
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

    /**
     * Updates a product.
     * @param product The product to update.
     * @throws IOException If an I/O error occurs.
     */
    public void updateProduct(Product product) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String productJson = mapper.writeValueAsString(product);
        SessionManager sessionManager = SessionManager.getInstance();
        String bearerToken = sessionManager.getToken();

        RequestBody body = RequestBody.create(productJson, JSON);

        Request request = new Request.Builder()
                .url(API_URL + "api/products/update")
                .put(body)
                .addHeader("Authorization", "Bearer " + bearerToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to update product: " + response);
            }
        }
    }

    /**
     * Deletes a product.
     * @param product The product to delete.
     * @param id The ID of the restaurant.
     * @throws IOException If an I/O error occurs.
     */
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
