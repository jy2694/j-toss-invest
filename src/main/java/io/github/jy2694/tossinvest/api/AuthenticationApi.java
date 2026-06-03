package io.github.jy2694.tossinvest.api;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import io.github.jy2694.tossinvest.exception.AuthenticationException;
import io.github.jy2694.tossinvest.exception.InvalidRequestException;
import io.github.jy2694.tossinvest.exception.TossApiException;
import io.github.jy2694.tossinvest.model.auth.OAuth2ErrorResponse;
import io.github.jy2694.tossinvest.model.auth.OAuth2TokenResponse;
import io.github.jy2694.tossinvest.model.common.ApiError;
import io.github.jy2694.tossinvest.http.HttpMethod;
import io.github.jy2694.tossinvest.http.TossRequest;
import io.github.jy2694.tossinvest.http.TossResponse;

/**
 * OAuth2 token issuance. This endpoint uses the OAuth2 standard request/response
 * format rather than the common {@code ApiResponse}/{@code ErrorResponse} envelope,
 * so it does not reuse {@link TossApi}'s envelope helpers.
 */
public interface AuthenticationApi extends TossApi {

    /**
     * Issues an access token via the OAuth2 client credentials grant.
     *
     * @param clientId the issued client id
     * @param clientSecret the issued client secret
     * @return the token response (access token, type, and expiry)
     * @throws InvalidRequestException on HTTP 400 (malformed request)
     * @throws AuthenticationException on HTTP 401 (invalid credentials)
     */
    default OAuth2TokenResponse issueToken(String clientId, String clientSecret) {
        String formBody = "grant_type=client_credentials"
                + "&client_id=" + encode(clientId)
                + "&client_secret=" + encode(clientSecret);

        TossRequest request = TossRequest.builder()
                .method(HttpMethod.POST)
                .path("/oauth2/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(formBody)
                .build();

        TossResponse response = transport().send(request);
        if (response.isSuccessful()) {
            return readValue(response.body(), OAuth2TokenResponse.class);
        }
        throw toAuthException(response);
    }

    private TossApiException toAuthException(TossResponse response) {
        OAuth2ErrorResponse oauthError = readValue(response.body(), OAuth2ErrorResponse.class);
        ApiError error = oauthError == null ? null
                : new ApiError(null, oauthError.error(), oauthError.errorDescription(), null);
        return switch (response.statusCode()) {
            case 401 -> new AuthenticationException(401, error);
            default -> new InvalidRequestException(response.statusCode(), error);
        };
    }

    private <T> T readValue(String body, Class<T> type) {
        try {
            return objectMapper().readValue(body, type);
        } catch (Exception e) {
            throw new TossApiException("Failed to parse OAuth2 response", e);
        }
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
