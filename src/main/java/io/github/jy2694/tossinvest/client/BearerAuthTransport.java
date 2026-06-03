package io.github.jy2694.tossinvest.client;

import java.util.function.Supplier;

import io.github.jy2694.tossinvest.http.HttpTransport;
import io.github.jy2694.tossinvest.http.TossRequest;
import io.github.jy2694.tossinvest.http.TossResponse;

/**
 * Wraps another {@link HttpTransport}, adding the {@code Authorization: Bearer}
 * header to every request that doesn't already carry one and isn't the token
 * endpoint. The token is read lazily so it can be refreshed without rebuilding
 * the transport.
 */
final class BearerAuthTransport implements HttpTransport {

    private final HttpTransport delegate;
    private final Supplier<String> tokenSupplier;

    BearerAuthTransport(HttpTransport delegate, Supplier<String> tokenSupplier) {
        this.delegate = delegate;
        this.tokenSupplier = tokenSupplier;
    }

    @Override
    public TossResponse send(TossRequest request) {
        return delegate.send(maybeAuthorize(request));
    }

    private TossRequest maybeAuthorize(TossRequest request) {
        String token = tokenSupplier.get();
        boolean tokenEndpoint = request.path().startsWith("/oauth2/");
        if (token == null || tokenEndpoint || request.headers().containsKey("Authorization")) {
            return request;
        }

        TossRequest.Builder builder = TossRequest.builder()
                .method(request.method())
                .path(request.path())
                .body(request.body())
                .header("Authorization", "Bearer " + token);
        request.queryParams().forEach(builder::query);
        request.headers().forEach(builder::header);
        return builder.build();
    }
}
