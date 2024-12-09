package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import ofosFrontend.model.ContactInfo;
import ofosFrontend.model.LoginResponse;
import ofosFrontend.model.PasswordChangeDTO;
import ofosFrontend.model.User;
import ofosFrontend.session.SessionManager;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Service class for handling user operations
 */
public class UserService {

    private static final String API_URL = "http://localhost:8000/api/";

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LogManager.getLogger(UserService.class);
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String MEDIA_TYPE = "application/json; charset=utf-8";

    /**
     * Logs in a user with the given username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The response from the server.
     * @throws IOException If an I/O error occurs.
     */
    public LoginResponse login(String username, String password) throws IOException {

        MediaType JSON = MediaType.get(MEDIA_TYPE);

        User user = new User(username, password);

        String jsonBody = mapper.writeValueAsString(user);

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(API_URL + "auth/login")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            int statusCode = response.code();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                Map<String, String> bodyMap = mapper.readValue(responseBody, Map.class);
                return new LoginResponse(statusCode, bodyMap);
            } else {
                throw new IOException("Failed to login. Status code: " + response.code());
            }
        }

    }

    /**
     * Registers a new user with the given username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The response from the server.
     * @throws IOException If an I/O error occurs.
     */
    public Response register(String username, String password) throws IOException {

        MediaType JSON = MediaType.get(MEDIA_TYPE);

        User user = new User(username, password);

        String jsonBody = mapper.writeValueAsString(user);

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(API_URL + "users/create")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response;
            } else {
                throw new IOException("Failed to register. Status code: " + response.code());
            }
        }
    }

    /**
     * Fetches the user data of the currently logged-in user.
     *
     * @param userId The ID of the user.
     * @return A Task that fetches the user data.
     */
    public Task<ContactInfo> fetchUserData(int userId) {
        return new Task<>() {
            @Override
            protected ContactInfo call() throws Exception {
                String url = API_URL + "contactinfo/" + userId;

                Request request = new Request.Builder()
                        .url(url)
                        .header(AUTHORIZATION, BEARER + SessionManager.getInstance().getToken())
                        .get()
                        .build();


                try (Response response = client.newCall(request).execute()) {
                    if (response.code() == 200) {


                        return mapper.readValue(response.body().string(), ContactInfo.class);
                    } else if (response.code() == 404) {

                        return null;
                    } else {
                        throw new IOException("Failed to fetch contact information. Status code: " + response.code());
                    }
                }
            }
        };
    }

    /**
     * Updates the password of the currently logged-in user.
     *
     * @param passwordDTO The DTO containing the old and new passwords.
     * @return A Task that updates the password.
     */
    public Task<Void> updatePassword(PasswordChangeDTO passwordDTO) {
        return new Task<>() {
            @Override
            protected Void call() throws IOException {
                String url = API_URL + "users/updatePassword";
                String token = SessionManager.getInstance().getToken();

                String requestBody = mapper.writeValueAsString(passwordDTO);

                Request request = new Request.Builder()
                        .url(url)
                        .header("Content-Type", "application/json")
                        .header(AUTHORIZATION, BEARER + token)
                        .put(RequestBody.create(requestBody, MediaType.get(MEDIA_TYPE)))
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Failed to update password. Status code: " + response.code());
                    }
                } catch (IOException e) {
                    throw new IOException("Network error occurred while updating password", e);
                }

                return null;
            }
        };
    }

    /**
     * Fetches all users.
     *
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
     *
     * @return A Task that deletes the user.
     */
    public Task<Void> deleteUser() {
        return new Task<>() {
            @Override
            protected Void call() throws IOException {
                String url = API_URL + "users/delete";
                String token = SessionManager.getInstance().getToken();

                Request request = new Request.Builder()
                        .url(url)
                        .header(AUTHORIZATION, BEARER + token)
                        .delete()
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.code() == 418) {
                        throw new IOException("Owner accounts cannot be deleted. Status code: " + response.code());
                    } else if (!response.isSuccessful()) {
                        throw new IOException("Failed to delete user. Status code: " + response.code());
                    }
                }

                return null;
            }
        };
    }

    /**
     * Fetches user information by username.
     *
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
     *
     * @param userId The ID of the user to ban.
     * @return True if the operation succeeded, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    public boolean banUser(int userId) throws IOException {
        MediaType JSON = MediaType.get(MEDIA_TYPE);

        Request request = new Request.Builder()
                .url(API_URL + "users/ban/" + userId)
                .post(RequestBody.create("", JSON))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                logger.error("Failed to ban user: {} {}", response.code(), response.message());
                return false;
            }
        }
    }


    /**
     * Changes the role of a user by ID.
     *
     * @param userId  The ID of the user to change the role of.
     * @param newRole The new role of the user.
     * @return True if the operation succeeded, false otherwise.
     */
    public boolean changeRole(int userId, String newRole) {
        String url = API_URL + "users/changerole";
        String token = SessionManager.getInstance().getToken();

        if (token == null || token.isEmpty()) {
            logger.error("Cannot change role: Token is null or empty.");
            return false;
        }

        try {
            // Construct JSON request body
            User user = new User();
            user.setRole(newRole);
            user.setUserId(userId);

            String requestBody = mapper.writeValueAsString(user);

            RequestBody body = RequestBody.create(
                    requestBody,
                    MediaType.get(MEDIA_TYPE)
            );

            // Build the request
            Request request = new Request.Builder()
                    .url(url)
                    .header("Content-Type", "application/json")
                    .header(AUTHORIZATION, BEARER + token)
                    .put(body)
                    .build();

            // Execute the request
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    logger.info("Role changed successfully for user ID: {}", userId);
                    return true;
                } else {
                    String errorResponse = response.body() != null ? response.body().string() : "No response body";
                    logger.error("Failed to change role. Server responded with: {} - {}", response.code(), errorResponse);
                    return false;
                }
            }

        } catch (IOException e) {
            logger.error("Failed to change role. Network error occurred.", e);
            return false;
        }
    }
}