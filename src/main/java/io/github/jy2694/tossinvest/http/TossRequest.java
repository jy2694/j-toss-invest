package io.github.jy2694.tossinvest.http;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An immutable HTTP request to the Toss Invest API, built through {@link #builder()}.
 * {@code body} is an arbitrary object serialized to JSON, or a pre-encoded
 * {@code String} (e.g. form bodies); {@code null} means no body.
 */
public final class TossRequest {

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> queryParams;
    private final Map<String, String> headers;
    private final Object body;

    private TossRequest(Builder builder) {
        this.method = builder.method;
        this.path = builder.path;
        this.queryParams = Map.copyOf(builder.queryParams);
        this.headers = Map.copyOf(builder.headers);
        this.body = builder.body;
    }

    public static Builder builder() {
        return new Builder();
    }

    public HttpMethod method() {
        return method;
    }

    public String path() {
        return path;
    }

    public Map<String, String> queryParams() {
        return queryParams;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public Object body() {
        return body;
    }

    public static final class Builder {

        private HttpMethod method = HttpMethod.GET;
        private String path;
        private final Map<String, String> queryParams = new LinkedHashMap<>();
        private final Map<String, String> headers = new LinkedHashMap<>();
        private Object body;

        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder query(String key, String value) {
            if (value != null) {
                queryParams.put(key, value);
            }
            return this;
        }

        public Builder header(String key, String value) {
            if (value != null) {
                headers.put(key, value);
            }
            return this;
        }

        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        public TossRequest build() {
            if (path == null || path.isBlank()) {
                throw new IllegalStateException("path is required");
            }
            return new TossRequest(this);
        }
    }
}
