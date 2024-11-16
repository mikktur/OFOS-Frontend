package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ofosFrontend.model.Restaurant;
import ofosFrontend.session.SessionManager;
import okhttp3.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantService {
    private static final String API_URL = "http://10.120.32.94:8000/"; //

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    public List<Restaurant> getAllRestaurants() throws IOException {

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(API_URL + "restaurants")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        return mapper.readValue(responseBody, mapper.getTypeFactory().constructCollectionType(List.class, Restaurant.class));

    }

    public List<Restaurant> getOwnerRestaurants() throws IOException {

        SessionManager sessionManager = SessionManager.getInstance();
        int ownerId = sessionManager.getUserId();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(API_URL + "restaurants/owner/" + ownerId)
                .get()
                .build();
        System.out.println(request);
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        List<Restaurant> restaurants = mapper.readValue(responseBody, mapper.getTypeFactory().constructCollectionType(List.class, Restaurant.class));
        System.out.println(restaurants);
        return restaurants;
    }
    public void updateRestaurantInfo(Restaurant restaurant) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String restaurantJson = mapper.writeValueAsString(restaurant);

        RequestBody body = RequestBody.create(restaurantJson, JSON);

        Request request = new Request.Builder()
                .url(API_URL + "restaurants/" + restaurant.getId())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
    }
    public void getRestaurantID(Restaurant restaurant) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String restaurantJson = mapper.writeValueAsString(restaurant);

        RequestBody body = RequestBody.create(restaurantJson, JSON);

        Request request = new Request.Builder()
                .url(API_URL + "restaurants/" + restaurant.getId())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
    }

    public List<Restaurant> getRestaurantsByCategory(String categoryName) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        // Encode the category name to handle spaces and special characters
        String encodedCategoryName = java.net.URLEncoder.encode(categoryName, "UTF-8");

        // Build the request URL
        String url = API_URL + "restaurants/category/" + encodedCategoryName;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        // Execute the request
        Response response = client.newCall(request).execute();

        // Check if the response is successful
        if (!response.isSuccessful()) {
            if (response.code() == 404) {
                // Category not found or no restaurants in category
                return new ArrayList<>();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }

        String responseBody = response.body().string();

        // Parse the JSON response into a list of Restaurant objects
        return mapper.readValue(responseBody,
                mapper.getTypeFactory().constructCollectionType(List.class, Restaurant.class));
    }

    public boolean changeOwner(int restaurantId, int newOwnerId) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String jsonBody = String.format("{\"restaurantId\": %d, \"newOwnerId\": %d}", restaurantId, newOwnerId);

        RequestBody body = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url(API_URL + "restaurants/changeowner")
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                System.err.println("Change owner failed: " + response.code() + " " + response.message());
                throw new IOException("Failed to change owner: " + response.code() + " " + response.message());
            }
        }
    }


    public boolean saveRestaurant(Restaurant restaurant) {
        String url = API_URL + "restaurants/createNew";
        String token = SessionManager.getInstance().getToken();

        // Create JSON representation of the restaurant
        String requestBody = String.format(
                "{\"name\":\"%s\",\"phone\":\"%s\",\"picture\":\"%s\",\"address\":\"%s\",\"hours\":\"%s\",\"ownerId\":%d}",
                restaurant.getRestaurantName(),
                restaurant.getRestaurantPhone(),
                restaurant.getPicture(),
                restaurant.getAddress(),
                restaurant.getBusinessHours(),
                restaurant.getOwnerId()
        );

        try {
            // Create the HTTP client
            HttpClient client = HttpClient.newHttpClient();

            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the response status
            if (response.statusCode() == 200) {
                System.out.println("Restaurant created successfully: " + response.body());
                return true;
            } else {
                System.err.println("Failed to create restaurant. Status code: " + response.statusCode());
                System.err.println("Response body: " + response.body());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Failure
        }
    }


}








