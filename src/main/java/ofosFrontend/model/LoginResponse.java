package ofosFrontend.model;

import java.util.Map;

public class LoginResponse {
    private final int statusCode;
    private final Map<String, String> body;

    public LoginResponse(int statusCode, Map<String, String> body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getBody() {
        return body;
    }
}