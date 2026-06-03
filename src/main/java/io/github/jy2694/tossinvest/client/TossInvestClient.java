package io.github.jy2694.tossinvest.client;

import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jy2694.tossinvest.api.AccountApi;
import io.github.jy2694.tossinvest.api.AuthenticationApi;
import io.github.jy2694.tossinvest.api.MarketDataApi;
import io.github.jy2694.tossinvest.api.OrderApi;
import io.github.jy2694.tossinvest.http.HttpTransport;
import io.github.jy2694.tossinvest.http.JdkHttpTransport;
import io.github.jy2694.tossinvest.model.auth.OAuth2TokenResponse;

/**
 * Entry point for the Toss Invest Open API. Aggregates every feature API and
 * holds the access token, which it attaches to authenticated requests.
 *
 * <pre>{@code
 * TossInvestClient client = TossInvestClient.create(clientId, clientSecret);
 * client.authenticate();
 * List<Account> accounts = client.getAccounts();
 * }</pre>
 */
public class TossInvestClient implements AuthenticationApi, MarketDataApi, AccountApi, OrderApi {

    private static final String DEFAULT_BASE_URL = "https://openapi.tossinvest.com";

    private final ObjectMapper objectMapper;
    private final HttpTransport transport;
    private final String clientId;
    private final String clientSecret;

    private volatile String accessToken;

    private TossInvestClient(String clientId, String clientSecret, String baseUrl) {
        this.clientId = Objects.requireNonNull(clientId, "clientId");
        this.clientSecret = Objects.requireNonNull(clientSecret, "clientSecret");
        this.objectMapper = TossJson.newMapper();
        HttpTransport jdk = new JdkHttpTransport(baseUrl, objectMapper);
        this.transport = new BearerAuthTransport(jdk, () -> accessToken);
    }

    /** Creates a client against the production API. */
    public static TossInvestClient create(String clientId, String clientSecret) {
        return new TossInvestClient(clientId, clientSecret, DEFAULT_BASE_URL);
    }

    /** Creates a client against a custom base URL (e.g. a mock server in tests). */
    public static TossInvestClient create(String clientId, String clientSecret, String baseUrl) {
        return new TossInvestClient(clientId, clientSecret, baseUrl);
    }

    /**
     * Issues an access token with the configured credentials and stores it for
     * subsequent calls.
     *
     * @return the token response (token type and expiry)
     */
    public OAuth2TokenResponse authenticate() {
        OAuth2TokenResponse token = issueToken(clientId, clientSecret);
        this.accessToken = token.accessToken();
        return token;
    }

    /** The current access token, or {@code null} if not yet authenticated. */
    public String accessToken() {
        return accessToken;
    }

    /** Sets the access token directly (e.g. one obtained out of band). */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /** {@inheritDoc} The returned transport attaches the bearer token automatically. */
    @Override
    public HttpTransport transport() {
        return transport;
    }

    /** {@inheritDoc} */
    @Override
    public ObjectMapper objectMapper() {
        return objectMapper;
    }
}
