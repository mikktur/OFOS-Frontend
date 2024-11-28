package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import ofosFrontend.model.ContactInfo;
import ofosFrontend.session.SessionManager;
import okhttp3.*;

import java.io.IOException;

public class ContactInfoService {
    private static final String API_URL = "http://10.120.32.94:8000/api/";
    private final ObjectMapper mapper = new ObjectMapper();
    private final OkHttpClient client = new OkHttpClient();

    public Task<Void> saveContactInfo(ContactInfo contactInfo) {
        return new Task<>() {
            @Override
            protected Void call() throws IOException {
                String url = API_URL + "contactinfo/save";
                String token = SessionManager.getInstance().getToken();

                // Serialize the ContactInfo object to JSON
                String requestBody = mapper.writeValueAsString(contactInfo);

                // Build the POST request
                Request request = new Request.Builder()
                        .url(url)
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .post(RequestBody.create(requestBody, MediaType.get("application/json")))
                        .build();

                // Execute the request and handle the response
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Failed to save contact information. Status code: " + response.code());
                    }
                }

                return null;
            }
        };
    }
}

