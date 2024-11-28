package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ofosFrontend.model.Product;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class ProductService {
    private static final String API_URL = "http://localhost:8000/"; //
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LogManager.getLogger(ProductService.class);
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String MEDIA_TYPE = "application/json; charset=utf-8";
    public List<Product> getProductsByRID(int id) throws IOException {


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
        MediaType JSON = MediaType.get(MEDIA_TYPE);

        String json = mapper.writeValueAsString(product);

        SessionManager sessionManager = SessionManager.getInstance();
        String bearerToken = sessionManager.getToken();

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(API_URL + "api/products/create/" + restaurantId)
                .post(body)
                .addHeader(AUTHORIZATION, BEARER + bearerToken)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Failed to add product: " + response);
        }
    }

    public void updateProduct(Product product) throws IOException {
        MediaType JSON = MediaType.get(MEDIA_TYPE);
        String productJson = mapper.writeValueAsString(product);
        SessionManager sessionManager = SessionManager.getInstance();
        String bearerToken = sessionManager.getToken();


        RequestBody body = RequestBody.create(productJson, JSON);

        Request request = new Request.Builder()
                .url(API_URL + "api/products/update")
                .put(body)
                .addHeader(AUTHORIZATION, BEARER + bearerToken)
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
        logger.info("testiiii");
        Request request = new Request.Builder()
                .url(API_URL + "api/products/delete/"+product.getProductID()+"/restaurant/" + id)
                .delete()
                .addHeader(AUTHORIZATION, BEARER + bearerToken)
                .build();

        try (Response response = client.newCall(request).execute()) {


            if (!response.isSuccessful()) {
                throw new IOException("Failed to delete product: " + response);
            }
        }

    }
}
