package io.github.jy2694.tossinvest.exception;

import io.github.jy2694.tossinvest.model.common.ApiError;

/**
 * Thrown when the API returns a non-2xx response with an error envelope.
 * Carries the HTTP status and the parsed {@link ApiError} so callers can
 * inspect {@code code}/{@code requestId} for diagnostics.
 */
public class TossApiStatusException extends TossApiException {

    private final int statusCode;
    private final transient ApiError error;

    public TossApiStatusException(int statusCode, ApiError error) {
        super(buildMessage(statusCode, error));
        this.statusCode = statusCode;
        this.error = error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ApiError getError() {
        return error;
    }

    /** Machine-readable error code (e.g. {@code invalid-request}), or {@code null}. */
    public String getCode() {
        return error == null ? null : error.code();
    }

    /** Server request id for support, or {@code null}. */
    public String getRequestId() {
        return error == null ? null : error.requestId();
    }

    private static String buildMessage(int statusCode, ApiError error) {
        if (error == null) {
            return "HTTP " + statusCode;
        }
        return "HTTP " + statusCode + " [" + error.code() + "] " + error.message();
    }
}
