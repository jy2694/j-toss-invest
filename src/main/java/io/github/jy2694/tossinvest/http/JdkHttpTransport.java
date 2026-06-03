package io.github.jy2694.tossinvest.http;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jy2694.tossinvest.exception.TossApiException;

/** Default {@link HttpTransport} backed by the JDK {@link HttpClient}. */
public final class JdkHttpTransport implements HttpTransport {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final Duration requestTimeout;

    public JdkHttpTransport(String baseUrl, ObjectMapper objectMapper) {
        this(baseUrl, objectMapper, Duration.ofSeconds(10), Duration.ofSeconds(10));
    }

    public JdkHttpTransport(String baseUrl, ObjectMapper objectMapper,
                            Duration connectTimeout, Duration requestTimeout) {
        this.baseUrl = baseUrl;
        this.objectMapper = objectMapper;
        this.requestTimeout = requestTimeout;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .build();
    }

    @Override
    public TossResponse send(TossRequest request) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(buildUri(request))
                .timeout(requestTimeout)
                .method(request.method().name(), bodyPublisher(request.body()));

        request.headers().forEach(builder::header);

        try {
            HttpResponse<String> response = httpClient.send(
                    builder.build(),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return new TossResponse(response.statusCode(), response.body());
        } catch (IOException e) {
            throw new TossApiException("HTTP communication failed", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TossApiException("HTTP request interrupted", e);
        }
    }

    private HttpRequest.BodyPublisher bodyPublisher(Object body) {
        if (body == null) {
            return HttpRequest.BodyPublishers.noBody();
        }
        String payload = (body instanceof String s) ? s : writeJson(body);
        return HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8);
    }

    private String writeJson(Object body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new TossApiException("Failed to serialize request body", e);
        }
    }

    private URI buildUri(TossRequest request) {
        StringBuilder url = new StringBuilder(baseUrl).append(request.path());
        Map<String, String> query = request.queryParams();
        if (!query.isEmpty()) {
            String queryString = query.entrySet().stream()
                    .map(e -> encode(e.getKey()) + "=" + encode(e.getValue()))
                    .collect(Collectors.joining("&"));
            url.append('?').append(queryString);
        }
        return URI.create(url.toString());
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
