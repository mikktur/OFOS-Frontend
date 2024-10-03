package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ofosFrontend.model.Restaurant;
import ofosFrontend.session.SessionManager;
import okhttp3.*;

import java.io.IOException;
import java.util.List;

public class RestaurantService {
    private static final String API_URL = "http://localhost:8000/"; //

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

        // Serialize the restaurant object to JSON
        String restaurantJson = mapper.writeValueAsString(restaurant);

        // Create the request body with the JSON
        RequestBody body = RequestBody.create(restaurantJson, JSON);

        // Build the PUT request to update restaurant information
        Request request = new Request.Builder()
                .url(API_URL + "restaurants/" + restaurant.getId()) // Assuming the restaurant ID is used in the URL
                .put(body)
                .build();

        // Execute the request and handle the response
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
    }
}






