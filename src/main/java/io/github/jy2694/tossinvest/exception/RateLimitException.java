package io.github.jy2694.tossinvest.exception;

import io.github.jy2694.tossinvest.model.common.ApiError;

/** Rate limit exceeded (HTTP 429). */
public class RateLimitException extends TossApiStatusException {

    public RateLimitException(int statusCode, ApiError error) {
        super(statusCode, error);
    }
}
