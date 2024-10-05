package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import ofosFrontend.model.ContactInfo;
import ofosFrontend.model.User;
import ofosFrontend.session.SessionManager;
import okhttp3.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class UserService {

    private static final String API_URL = "http://localhost:8000/api/"; //

    private final OkHttpClient client = new OkHttpClient();

    public Response login(String username, String password) throws IOException {

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        User user = new User(username, password);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(user);

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(API_URL + "auth/login")
                .post(body)
                .build();

        return client.newCall(request).execute();
    }


    public Response register(String username, String password) throws IOException {

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        User user = new User(username, password);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(user);

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(API_URL + "users/create")
                .post(body)
                .build();

        return client.newCall(request).execute();
    }

    public Task<ContactInfo> fetchUserData(int userId) {
        return new Task<>() {
            @Override
            protected ContactInfo call() throws Exception {
                String url = "http://localhost:8000/api/contactinfo/" + userId;

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(response.body(), ContactInfo.class);
                } else if (response.statusCode() == 404) {
                    // No contact information found, return null
                    return null;
                } else {
                    throw new Exception("Failed to fetch contact information. Status code: " + response.statusCode());
                }
            }
        };
    }
}