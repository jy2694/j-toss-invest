package io.github.jy2694.tossinvest.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

/** OAuth2 token issuance failure response (OAuth2 standard error format). */
public record OAuth2ErrorResponse(
        String error,
        @JsonProperty("error_description") String errorDescription,
        @JsonProperty("error_uri") String errorUri) {
}
