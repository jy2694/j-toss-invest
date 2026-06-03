package io.github.jy2694.tossinvest.exception;

import io.github.jy2694.tossinvest.model.common.ApiError;

/** Authentication or authorization failure (HTTP 401). */
public class AuthenticationException extends TossApiStatusException {

    public AuthenticationException(int statusCode, ApiError error) {
        super(statusCode, error);
    }
}
