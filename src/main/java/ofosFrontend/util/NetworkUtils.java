package ofosFrontend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import ofosFrontend.model.User;
import okhttp3.*;

import java.io.IOException;


public class NetworkUtils {

    private static final String LOGIN_URL = "http://localhost:8000/api/"; // Replace with your backend URL

    private final OkHttpClient client = new OkHttpClient();

    public boolean login(String username, String password) throws IOException {
        // Define the media type for JSON
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        // Create a User object with the username and password
        User user = new User(username, password);
        // Serialize the User object to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(user);

        // Create the request body with JSON content
        RequestBody body = RequestBody.create(jsonBody, JSON);

        // Build the HTTP POST request
        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(body)
                .build();

        // Execute the request and handle the response
        try (Response response = client.newCall(request).execute()) {
            int statusCode = response.code(); // Get the HTTP status code

            // Handle different status codes
            if (statusCode == 200) { // HTTP 200 OK
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