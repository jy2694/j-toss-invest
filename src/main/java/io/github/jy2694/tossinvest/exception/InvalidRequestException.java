package io.github.jy2694.tossinvest.exception;

import io.github.jy2694.tossinvest.model.common.ApiError;

/** Request validation failure (HTTP 400 / 422). */
public class InvalidRequestException extends TossApiStatusException {

    public InvalidRequestException(int statusCode, ApiError error) {
        super(statusCode, error);
    }
}
