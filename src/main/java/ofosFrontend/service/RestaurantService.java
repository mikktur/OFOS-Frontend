package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;

public class RestaurantService {
    private static final String API_URL = "http://localhost:8000/"; //

    private final OkHttpClient client = new OkHttpClient();

    public Response getAllRestaurants() throws IOException {

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(API_URL + "restaurants")
                .get()
                .build();
        System.out.println(request);
        Response response = client.newCall(request).execute();

        return response;

    }
}





