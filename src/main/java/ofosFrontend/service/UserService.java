package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ofosFrontend.model.User;
import okhttp3.*;

import java.io.IOException;


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
}