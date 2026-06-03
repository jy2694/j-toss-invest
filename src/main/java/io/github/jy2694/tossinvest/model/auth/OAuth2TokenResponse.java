package io.github.jy2694.tossinvest.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

/** OAuth2 token issuance success response (OAuth2 standard, not the BFF envelope). */
public record OAuth2TokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") long expiresIn) {
}
