package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ofosFrontend.model.Product;
import ofosFrontend.session.LocalizationManager;
import ofosFrontend.session.SessionManager;
import okhttp3.*;

import java.io.IOException;
import java.util.List;

public class ProductService {
    private static final String API_URL = "http://10.120.32.94:8000/";
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Product> getProductsByRID(int id) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

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

        String productJson = mapper.writeValueAsString(product);

        addProductTranslations(product);

        SessionManager sessionManager = SessionManager.getInstance();
        String bearerToken = sessionManager.getToken();

        RequestBody body = RequestBody.create(productJson, JSON);
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

    private void addProductTranslations(Product product) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String finnishTranslation = product.getFinnishTranslation();
        String japaneseTranslation = product.getJapaneseTranslation();
        String russianTranslation = product.getRussianTranslation();

        String translationJson = "{\"productID\": " + product.getProductID() +
                ", \"finnish\": \"" + finnishTranslation + "\", " +
                "\"japanese\": \"" + japaneseTranslation + "\", " +
                "\"russian\": \"" + russianTranslation + "\"}";

        RequestBody translationBody = RequestBody.create(translationJson, JSON);
        Request request = new Request.Builder()
                .url(API_URL + "api/product_translations/create")
                .post(translationBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to add product translations: " + response);
            }
        }
    }

    public void updateProduct(Product product) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String productJson = mapper.writeValueAsString(product);
        SessionManager sessionManager = SessionManager.getInstance();
        String bearerToken = sessionManager.getToken();

        updateProductTranslations(product);

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

    private void updateProductTranslations(Product product) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String finnishTranslation = product.getFinnishTranslation();
        String japaneseTranslation = product.getJapaneseTranslation();
        String russianTranslation = product.getRussianTranslation();

        String translationJson = "{\"productID\": " + product.getProductID() +
                ", \"finnish\": \"" + finnishTranslation + "\", " +
                "\"japanese\": \"" + japaneseTranslation + "\", " +
                "\"russian\": \"" + russianTranslation + "\"}";

        RequestBody translationBody = RequestBody.create(translationJson, JSON);
        Request request = new Request.Builder()
                .url(API_URL + "api/product_translations/update")
                .put(translationBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to update product translations: " + response);
            }
        }
    }

    public void deleteProduct(Product product, int id) throws IOException {
        SessionManager sessionManager = SessionManager.getInstance();
        String bearerToken = sessionManager.getToken();

        deleteProductTranslations(product.getProductID());

        Request request = new Request.Builder()
                .url(API_URL + "api/products/delete/" + product.getProductID() + "/restaurant/" + id)
                .delete()
                .addHeader("Authorization", "Bearer " + bearerToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to delete product: " + response);
            }
        }
    }

    private void deleteProductTranslations(int productID) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String translationJson = "{\"productID\": " + productID + "}";

        RequestBody translationBody = RequestBody.create(translationJson, JSON);
        Request request = new Request.Builder()
                .url(API_URL + "api/product_translations/delete")
                .post(translationBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to delete product translations: " + response);
            }
        }
    }
}

