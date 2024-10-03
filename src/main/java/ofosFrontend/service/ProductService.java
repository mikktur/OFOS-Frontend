package ofosFrontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ofosFrontend.model.Product;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class ProductService {
    private static final String API_URL = "http://localhost:8000/"; //

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Product> getProductsByRID(int id) throws IOException {

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        Request request = new Request.Builder()
                .url(API_URL + "api/products/restaurant/"+id)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        List<Product> products = mapper.readValue(responseBody, mapper.getTypeFactory().constructCollectionType(List.class, Product.class));

        return products;

    }
}
