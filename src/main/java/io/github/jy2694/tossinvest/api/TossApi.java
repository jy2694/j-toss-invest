package io.github.jy2694.tossinvest.api;

import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jy2694.tossinvest.exception.AuthenticationException;
import io.github.jy2694.tossinvest.exception.InvalidRequestException;
import io.github.jy2694.tossinvest.exception.RateLimitException;
import io.github.jy2694.tossinvest.exception.TossApiException;
import io.github.jy2694.tossinvest.exception.TossApiStatusException;
import io.github.jy2694.tossinvest.model.common.ApiError;
import io.github.jy2694.tossinvest.model.common.ErrorResponse;
import io.github.jy2694.tossinvest.http.HttpTransport;
import io.github.jy2694.tossinvest.http.TossRequest;
import io.github.jy2694.tossinvest.http.TossResponse;

/**
 * Shared plumbing for every feature API: it sends the request, unwraps the
 * {@code ApiResponse} envelope, and turns non-2xx responses into typed
 * exceptions. Feature interfaces extend this and add {@code default} methods.
 */
public interface TossApi {

    /** The transport used to send requests; implementations attach the bearer token here. */
    HttpTransport transport();

    /** The mapper used to (de)serialize request and response bodies. */
    ObjectMapper objectMapper();

    /** Renders an optional query value, returning {@code null} so it is omitted. */
    static String toStringOrNull(Object value) {
        return value == null ? null : value.toString();
    }

    /** Sends the request and returns the {@code result} payload parsed as {@code T}. */
    default <T> T executeForObject(TossRequest request, Class<T> type) {
        JsonNode result = sendAndUnwrap(request);
        return convert(result, objectMapper().constructType(type));
    }

    /** Sends the request and returns the {@code result} payload parsed as {@code List<T>}. */
    default <T> List<T> executeForList(TossRequest request, Class<T> elementType) {
        JsonNode result = sendAndUnwrap(request);
        JavaType listType = objectMapper()
                .getTypeFactory()
                .constructCollectionType(List.class, elementType);
        if (result == null || result.isNull()) {
            return List.of();
        }
        return convert(result, listType);
    }

    /** Sends the request and parses the whole body as {@code T} (no envelope unwrapping). */
    default <T> T executeRaw(TossRequest request, Class<T> type) {
        TossResponse response = transport().send(request);
        if (!response.isSuccessful()) {
            throw toException(response);
        }
        return readValue(response.body(), type);
    }

    private JsonNode sendAndUnwrap(TossRequest request) {
        TossResponse response = transport().send(request);
        if (!response.isSuccessful()) {
            throw toException(response);
        }
        JsonNode root = readTree(response.body());
        return root.get("result");
    }

    private <T> T convert(JsonNode node, JavaType type) {
        if (node == null) {
            return null;
        }
        try {
            return objectMapper().convertValue(node, type);
        } catch (IllegalArgumentException e) {
            throw new TossApiException("Failed to parse response payload", e);
        }
    }

    private <T> T readValue(String body, Class<T> type) {
        try {
            return objectMapper().readValue(body, type);
        } catch (Exception e) {
            throw new TossApiException("Failed to parse response body", e);
        }
    }

    private JsonNode readTree(String body) {
        try {
            return objectMapper().readTree(body);
        } catch (Exception e) {
            throw new TossApiException("Failed to parse response body", e);
        }
    }

    private TossApiStatusException toException(TossResponse response) {
        ApiError error = parseError(response.body());
        return switch (response.statusCode()) {
            case 400, 422 -> new InvalidRequestException(response.statusCode(), error);
            case 401 -> new AuthenticationException(response.statusCode(), error);
            case 429 -> new RateLimitException(response.statusCode(), error);
            default -> new TossApiStatusException(response.statusCode(), error);
        };
    }

    private ApiError parseError(String body) {
        if (body == null || body.isBlank()) {
            return null;
        }
        try {
            ErrorResponse envelope = objectMapper().readValue(body, ErrorResponse.class);
            return envelope.error();
        } catch (Exception e) {
            return null;
        }
    }
}
