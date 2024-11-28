package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import ofosFrontend.model.ContactInfo;
import ofosFrontend.model.PasswordChangeDTO;
import ofosFrontend.model.User;
import ofosFrontend.session.SessionManager;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;


public class UserService {

    private static final String API_URL = "http://10.120.32.94:8000/api/";

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LogManager.getLogger(UserService.class);
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String MEDIA_TYPE = "application/json; charset=utf-8";

    public Response login(String username, String password) throws IOException {

        MediaType JSON = MediaType.get(MEDIA_TYPE);

        User user = new User(username, password);

        String jsonBody = mapper.writeValueAsString(user);

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(API_URL + "auth/login")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response;
            } else {
                throw new IOException("Failed to login. Status code: " + response.code());
            }
        }

    }

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
                    if (response.code() == 418) { // Status code for "I'm a teapot"
                        throw new IOException("Owner accounts cannot be deleted. Status code: " + response.code());
                    } else if (!response.isSuccessful()) {
                        throw new IOException("Failed to delete user. Status code: " + response.code());
                    }
                }

                return null;
            }
        };
    }

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


    public boolean changeRole(int userId, String newRole) {
        String url = API_URL + "users/changeRole";
        String token = SessionManager.getInstance().getToken();

        String requestBody = "{\"userId\":" + userId + ",\"newRole\":\"" + newRole + "\"}";

        RequestBody body = RequestBody.create(
                requestBody,
                MediaType.get(MEDIA_TYPE)
        );

        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .header(AUTHORIZATION, BEARER + token)
                .put(body)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                logger.info("Role changed successfully for user ID: {}", userId);
                return true;
            } else {
                logger.error("Failed to change role. Server responded with: {}", response.code());
                return false;
            }

        } catch (IOException e) {
            logger.error("Failed to change role. Network error occurred: {}", e.getMessage());
            return false;
        }
    }
}