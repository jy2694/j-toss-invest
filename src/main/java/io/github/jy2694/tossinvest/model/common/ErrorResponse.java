package io.github.jy2694.tossinvest.model.common;

/** Error envelope used by 4xx/5xx responses. */
public record ErrorResponse(ApiError error) {
}
