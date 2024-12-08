package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ofosFrontend.model.Restaurant;
import ofosFrontend.session.SessionManager;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


/**
 * Service class for handling restaurant operations
 */
public class RestaurantService {
    private static final String API_URL = "http://localhost:8000/"; //
    private final Logger logger = LogManager.getLogger(RestaurantService.class);
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String MEDIA_TYPE = "application/json; charset=utf-8";


    /**
     * Fetches all restaurants from the API.
     *
     * @return A list of Restaurant objects.
     * @throws IOException If an I/O error occurs.
     */
    public List<Restaurant> getAllRestaurants() throws IOException {


        Request request = new Request.Builder()
                .url(API_URL + "restaurants")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        return mapper.readValue(responseBody, mapper.getTypeFactory().constructCollectionType(List.class, Restaurant.class));

    }

    /**
     * Fetches restaurants of a certain owner.
     *
     * @return A list of Restaurant objects.
     * @throws IOException If an I/O error occurs.
     */
    public List<Restaurant> getOwnerRestaurants() throws IOException {

        SessionManager sessionManager = SessionManager.getInstance();
        int ownerId = sessionManager.getUserId();


        Request request = new Request.Builder()
                .url(API_URL + "restaurants/owner/" + ownerId)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        return mapper.readValue(responseBody, mapper.getTypeFactory().constructCollectionType(List.class, Restaurant.class));

    }


    /**
     * Updates a restaurant's information.
     *
     * @param restaurant The restaurant to fetch.
     * @throws IOException If an I/O error occurs.
     */
    public void updateRestaurantInfo(Restaurant restaurant) throws IOException {
        MediaType JSON = MediaType.get(MEDIA_TYPE);

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


    /**
     * Fetches restaurants of a certain category.
     *
     * @param categoryName The category name.
     * @return A list of Restaurant objects.
     * @throws IOException If an I/O error occurs.
     */
    public List<Restaurant> getRestaurantsByCategory(String categoryName) throws IOException {


        // Encode the category name to handle spaces and special characters
        String encodedCategoryName = java.net.URLEncoder.encode(categoryName, "UTF-8");

        String url = API_URL + "restaurants/category/" + encodedCategoryName;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();

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

    /**
     * Change the owner of a restaurant.
     *
     * @param restaurantId The ID of the restaurant.
     * @param newOwnerId   The ID of the new owner.
     * @return True if the operation was successful, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    public boolean changeOwner(int restaurantId, int newOwnerId) throws IOException {
        MediaType JSON = MediaType.get(MEDIA_TYPE);
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
                logger.error("Change owner failed: {} {}", response.code(), response.message());
                throw new IOException("Failed to change owner: " + response.code() + " " + response.message());
            }
        }
    }

    /**
     * Saves a new restaurant to the database.
     *
     * @param restaurant The restaurant to save.
     * @return True if the operation was successful, false otherwise.
     */
    public boolean saveRestaurant(Restaurant restaurant) {
        String url = API_URL + "restaurants/create";
        String token = SessionManager.getInstance().getToken();
        String requestBody;
        try {
            requestBody = mapper.writeValueAsString(restaurant);
        } catch (IOException e) {
            logger.error("Error while serializing restaurant: {}", e.getMessage());
            return false;
        }

        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .post(RequestBody.create(requestBody, MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("Failed to create restaurant. Status code: {}", response.code());
                return false;
            }

            String responseBody = response.body().string();
            logger.info("Restaurant created successfully: {}", responseBody);
            return true;
        } catch (IOException e) {
            logger.error("Error while saving restaurant: {}", e.getMessage());
            return false;
        }
    }

}








