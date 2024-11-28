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

/**
 * Service class for handling user operations
 */
public class UserService {

    private static final String API_URL = "http://10.120.32.94:8000/api/";
    private static final String API_URL_LOCAL = "http://localhost:8000/api/";

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Logs in a user with the given username and password.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The response from the server.
     * @throws IOException If an I/O error occurs.
     */
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

    /**
     * Registers a new user with the given username and password.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The response from the server.
     * @throws IOException If an I/O error occurs.
     */
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

    /**
     * Fetches the user data of the currently logged-in user.
     * @param userId The ID of the user.
     * @return A Task that fetches the user data.
     */
    public Task<ContactInfo> fetchUserData(int userId) {
        return new Task<>() {
            @Override
            protected ContactInfo call() throws Exception {
                String url = API_URL + "contactinfo/" + userId;

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

    /**
     * Updates the password of the currently logged-in user.
     * @param passwordDTO The DTO containing the old and new passwords.
     * @return A Task that updates the password.
     */
    public Task<Void> updatePassword(PasswordChangeDTO passwordDTO) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                String url = API_URL + "users/updatePassword";
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

    /**
     * Fetches all users.
     * @return A list of User objects.
     * @throws IOException If an I/O error occurs.
     */
    public List<User> getAllUsers() throws IOException {
        Request request = new Request.Builder()
                .url(API_URL + "users")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Failed to fetch users: " + response.code() + " " + response.message());
        }

        String responseBody = response.body().string();
        return mapper.readValue(responseBody, mapper.getTypeFactory().constructCollectionType(List.class, User.class));
    }

    /**
     * Deletes the currently logged-in user.
     * Checks if the user is an owner account and throws an exception if so.
     * @return A Task that deletes the user.
     */
    public Task<Void> deleteUser() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                String url = API_URL + "users/delete";
                String token = SessionManager.getInstance().getToken();

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bearer " + token)
                        .DELETE()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 418) {
                    throw new Exception("Owner accounts cannot be deleted. Status code: " + response.statusCode());
                } else if (response.statusCode() != 200) {
                    throw new Exception("Failed to delete user. Status code: " + response.statusCode());

                }

                return null;
            }
        };
    }

    /**
     * Fetches user information by username.
     * @param selectedUserName The username of the user to fetch.
     * @return The User object.
     * @throws IOException If an I/O error occurs.
     */
    public User getUserByUsername(String selectedUserName) throws IOException {
        Request request = new Request.Builder()
                .url(API_URL + "users/username/" + selectedUserName)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to fetch user: " + response.code() + " " + response.message());
            }

            String responseBody = response.body().string();

            return mapper.readValue(responseBody, User.class);
        }
    }

    /**
     * Bans a user by ID.
     * @param userId The ID of the user to ban.
     * @return True if the operation succeeded, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    public boolean banUser(int userId) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        Request request = new Request.Builder()
                .url(API_URL + "users/ban/" + userId)
                .post(RequestBody.create("", JSON))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                System.err.println("Failed to ban user: " + response.code() + " " + response.message());
                return false;
            }
        }
    }

    /**
     * Changes the role of a user by ID.
     * @param userId The ID of the user to change the role of.
     * @param newRole The new role of the user.
     * @return True if the operation succeeded, false otherwise.
     */
    public boolean changeRole(int userId, String newRole) {
        String url = API_URL + "users/changeRole";
        String token = SessionManager.getInstance().getToken();

        // Create a JSON request body
        String requestBody = "{\"userId\":" + userId + ",\"newRole\":\"" + newRole + "\"}";

        try {
            // Create the HTTP client
            HttpClient client = HttpClient.newHttpClient();

            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();


            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}