package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import ofosFrontend.model.ContactInfo;
import ofosFrontend.session.SessionManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Service class for handling contact information operations
 */
public class ContactInfoService {
    private static final String API_URL = "http://10.120.32.94:8000/api/";


    /**
     * Saves the contact information of the currently logged-in user.
     * @param contactInfo The contact information to save.
     * @return A Task that saves the contact information.
     */
    public Task<Void> saveContactInfo(ContactInfo contactInfo) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                String url = API_URL + "contactinfo/save";
                String token = SessionManager.getInstance().getToken();

                ObjectMapper objectMapper = new ObjectMapper();
                String requestBody = objectMapper.writeValueAsString(contactInfo);

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new Exception("Failed to save contact information. Status code: " + response.statusCode());
                }

                return null;
            }
        };
    }
}

