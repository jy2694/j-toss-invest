package io.github.jy2694.tossinvest.model.common;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Error object inside the {@code ErrorResponse} envelope. Carries the minimal
 * identifying info plus an optional {@code data} hint.
 */
public record ApiError(
        String requestId,
        String code,
        String message,
        JsonNode data) {
}
