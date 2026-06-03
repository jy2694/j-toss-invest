package io.github.jy2694.tossinvest.http;

/** Raw HTTP response: status code plus the body as an unparsed JSON string. */
public record TossResponse(int statusCode, String body) {

    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }
}
