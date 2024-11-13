package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import ofosFrontend.model.ContactInfo;
import ofosFrontend.model.PasswordChangeDTO;
import ofosFrontend.model.User;
import ofosFrontend.session.SessionManager;
import okhttp3.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public class UserService {

    private static final String API_URL = "http://10.120.32.94:8000/api/"; //

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

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
                String url = "http://10.120.32.94:8000/api/contactinfo/" + userId;

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

    public Task<Void> updatePassword(PasswordChangeDTO passwordDTO) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                String url = "http://10.120.32.94:8000/api/users/updatePassword";
                String token = SessionManager.getInstance().getToken();

                ObjectMapper objectMapper = new ObjectMapper();
                String requestBody = objectMapper.writeValueAsString(passwordDTO);

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 400) {
                    throw new Exception("Unauthorized request. Status code: " + response.statusCode());
                }
                else if (response.statusCode() != 200) {
                    throw new Exception("Failed to update password. Status code: " + response.statusCode() + response.body());
                }

                return null;
            }
        };
    }

    public List<User> getAllUsers() throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        // Create a GET request
        Request request = new Request.Builder()
                .url(API_URL + "users")
                .get()
                .build();

        // Execute the request and parse the response
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Failed to fetch users: " + response.code() + " " + response.message());
        }

        String responseBody = response.body().string();
        return mapper.readValue(responseBody, mapper.getTypeFactory().constructCollectionType(List.class, User.class));
    }

    public Task<Void> deleteUser() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                String url = "http://10.120.32.94:8000/api/users/delete";
                String token = SessionManager.getInstance().getToken();

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bearer " + token)
                        .DELETE()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 418) { // Status code for "I'm a teapot"
                    throw new Exception("Owner accounts cannot be deleted. Status code: " + response.statusCode());
                } else if (response.statusCode() != 200) {
                    System.out.println("Delete user response: " + response.body());
                    throw new Exception("Failed to delete user. Status code: " + response.statusCode());

                }

                return null;
            }
        };
    }



}