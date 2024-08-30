package ofosFrontend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import ofosFrontend.model.User;
import okhttp3.*;

import java.io.IOException;


public class NetworkUtils {

    private static final String API_URL = "http://localhost:8000/api/"; //

    private final OkHttpClient client = new OkHttpClient();

    public boolean login(String username, String password) throws IOException {

        MediaType JSON = MediaType.get("application/json; charset=utf-8");


        User user = new User(username, password);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(user);

        System.out.println(jsonBody);
        RequestBody body = RequestBody.create(jsonBody, JSON);

        // Build the HTTP POST request
        Request request = new Request.Builder()
                .url(API_URL + "auth/login")
                .post(body)
                .build();

        // Execute the request and handle the response
        try (Response response = client.newCall(request).execute()) {
            System.out.println(response);
            int statusCode = response.code(); // Get the HTTP status code

            // Handle different status codes
            if (statusCode == 200) { // HTTP 200 OK
                assert response.body() != null;
                String responseBody = response.body().string();
                System.out.println(responseBody);
                return true;
            } else if (statusCode == 401) { // HTTP 401 Unauthorized
                // Handle unauthorized access (e.g., incorrect username/password)
                System.out.println("Login failed: Unauthorized");
                return false;
            } else if (statusCode == 500) { // HTTP 500 Internal Server Error
                // Handle server errors
                System.out.println("Server error occurred");
                return false;
            } else {
                // Handle other status codes
                System.out.println("Unexpected response code: " + statusCode);
                return false;
            }
        }
    }

    public boolean register(String username, String password) throws IOException {

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        User user = new User(username, password);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(user);

        System.out.println(jsonBody);
        RequestBody body = RequestBody.create(jsonBody, JSON);

        // Build the HTTP POST request
        Request request = new Request.Builder()
                .url(API_URL + "auth/login")
                .post(body)
                .build();

        // Execute the request and handle the response
        try (Response response = client.newCall(request).execute()) {
            System.out.println(response);
            int statusCode = response.code(); // Get the HTTP status code

            // Handle different status codes
            if (statusCode == 200) { // HTTP 200 OK
                assert response.body() != null;
                String responseBody = response.body().string();
                System.out.println(responseBody);
                return true;
            } else if (statusCode == 401) { // HTTP 401 Unauthorized
                // Handle unauthorized access (e.g., incorrect username/password)
                System.out.println("Login failed: Unauthorized");
                return false;
            } else if (statusCode == 500) { // HTTP 500 Internal Server Error
                // Handle server errors
                System.out.println("Server error occurred");
                return false;
            } else {
                // Handle other status codes
                System.out.println("Unexpected response code: " + statusCode);
                return false;
            }
        }

    }
}