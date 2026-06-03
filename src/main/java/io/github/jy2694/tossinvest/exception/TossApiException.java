package io.github.jy2694.tossinvest.exception;

/**
 * Base for every error raised by the Toss Invest client. Unchecked so callers
 * are not forced to wrap each call in {@code try/catch}.
 */
public class TossApiException extends RuntimeException {

    public TossApiException(String message) {
        super(message);
    }

    public TossApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
