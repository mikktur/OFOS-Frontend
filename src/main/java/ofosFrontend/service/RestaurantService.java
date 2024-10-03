package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ofosFrontend.model.Restaurant;
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


}





