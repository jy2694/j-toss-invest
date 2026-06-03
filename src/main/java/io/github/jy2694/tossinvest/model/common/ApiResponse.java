package io.github.jy2694.tossinvest.model.common;

/**
 * Success envelope used by 2xx responses. Every endpoint wraps its payload in
 * {@code result}.
 *
 * @param <T> the payload type
 */
public record ApiResponse<T>(T result) {
}
