package com.contextlayer.sdk;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class ContextClient {
    private final String apiKey;
    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper mapper;

    public ContextClient(String apiKey) {
        this(apiKey, "http://localhost:8136/api");
    }

    public ContextClient(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public String getContext(String customerId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/customers/" + customerId + "/context"))
            .header("X-App-Id", apiKey)
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
