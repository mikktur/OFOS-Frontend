package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import ofosFrontend.model.ContactInfo;
import ofosFrontend.session.SessionManager;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Service class for handling contact information operations
 */
public class ContactInfoService {
    private static final String API_URL = "http://localhost:8000/api/";
    private static final Logger logger = LogManager.getLogger(ContactInfoService.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final OkHttpClient client = new OkHttpClient();


    /**
     * Saves the contact information of the currently logged-in user.
     * @param contactInfo The contact information to save.
     * @return A Task that saves the contact information.
     */
    public Task<Void> saveContactInfo(ContactInfo contactInfo) {
        return new Task<>() {
            @Override
            protected Void call() throws IOException {
                String url = API_URL + "contactinfo/save";
                String token = SessionManager.getInstance().getToken();

                // Serialize the ContactInfo object to JSON
                String requestBody = mapper.writeValueAsString(contactInfo);
                logger.debug("Request body: " + requestBody);
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

