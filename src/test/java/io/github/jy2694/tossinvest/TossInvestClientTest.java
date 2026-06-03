package io.github.jy2694.tossinvest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.jy2694.tossinvest.client.TossInvestClient;
import io.github.jy2694.tossinvest.exception.AuthenticationException;
import io.github.jy2694.tossinvest.exception.RateLimitException;
import io.github.jy2694.tossinvest.model.account.Account;
import io.github.jy2694.tossinvest.model.account.AccountType;
import io.github.jy2694.tossinvest.model.auth.OAuth2TokenResponse;
import io.github.jy2694.tossinvest.model.market.CandleInterval;
import io.github.jy2694.tossinvest.model.query.CandleQuery;
import io.github.jy2694.tossinvest.model.query.OrderQuery;
import io.github.jy2694.tossinvest.model.query.OrderStatusFilter;

class TossInvestClientTest {

    private HttpServer server;
    private String baseUrl;
    private volatile String lastAuthHeader;
    private volatile String lastQuery;

    @BeforeEach
    void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        baseUrl = "http://127.0.0.1:" + server.getAddress().getPort();
    }

    @AfterEach
    void stopServer() {
        server.stop(0);
    }

    private void route(String path, int status, String body) {
        server.createContext(path, exchange -> {
            lastAuthHeader = exchange.getRequestHeaders().getFirst("Authorization");
            lastQuery = exchange.getRequestURI().getQuery();
            respond(exchange, status, body);
        });
    }

    private static void respond(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    @Test
    void authenticateStoresToken() {
        route("/oauth2/token", 200,
                "{\"access_token\":\"tok-123\",\"token_type\":\"Bearer\",\"expires_in\":3600}");
        server.start();

        TossInvestClient client = TossInvestClient.create("id", "secret", baseUrl);
        OAuth2TokenResponse token = client.authenticate();

        assertEquals("tok-123", token.accessToken());
        assertEquals("tok-123", client.accessToken());
    }

    @Test
    void getAccountsUnwrapsEnvelopeAndSendsBearer() {
        route("/api/v1/accounts", 200,
                "{\"result\":[{\"accountNo\":\"100-1\",\"accountSeq\":7,\"accountType\":\"BROKERAGE\"}]}");
        server.start();

        TossInvestClient client = TossInvestClient.create("id", "secret", baseUrl);
        client.setAccessToken("tok-123");

        List<Account> accounts = client.getAccounts();

        assertEquals(1, accounts.size());
        Account account = accounts.get(0);
        assertEquals("100-1", account.accountNo());
        assertEquals(7L, account.accountSeq());
        assertEquals(AccountType.BROKERAGE, account.accountType());
        assertEquals("Bearer tok-123", lastAuthHeader);
    }

    @Test
    void unknownEnumFallsBackToUnknown() {
        route("/api/v1/accounts", 200,
                "{\"result\":[{\"accountNo\":\"x\",\"accountSeq\":1,\"accountType\":\"BRAND_NEW_TYPE\"}]}");
        server.start();

        TossInvestClient client = TossInvestClient.create("id", "secret", baseUrl);
        List<Account> accounts = client.getAccounts();

        assertEquals(AccountType.UNKNOWN, accounts.get(0).accountType());
    }

    @Test
    void rateLimitMapsToRateLimitException() {
        route("/api/v1/accounts", 429,
                "{\"error\":{\"requestId\":\"req-9\",\"code\":\"rate-limited\",\"message\":\"slow down\"}}");
        server.start();

        TossInvestClient client = TossInvestClient.create("id", "secret", baseUrl);
        RateLimitException ex = assertThrows(RateLimitException.class, client::getAccounts);

        assertEquals(429, ex.getStatusCode());
        assertEquals("rate-limited", ex.getCode());
        assertEquals("req-9", ex.getRequestId());
        assertTrue(ex.getMessage().contains("slow down"));
    }

    @Test
    void unauthorizedMapsToAuthenticationException() {
        route("/api/v1/accounts", 401,
                "{\"error\":{\"requestId\":\"r\",\"code\":\"unauthorized\",\"message\":\"nope\"}}");
        server.start();

        TossInvestClient client = TossInvestClient.create("id", "secret", baseUrl);
        assertThrows(AuthenticationException.class, client::getAccounts);
    }

    @Test
    void candleQueryBuilderRendersQueryString() {
        route("/api/v1/candles", 200, "{\"result\":{\"candles\":[],\"nextBefore\":null}}");
        server.start();

        TossInvestClient client = TossInvestClient.create("id", "secret", baseUrl);
        client.getCandles(CandleQuery.of("005930", CandleInterval.ONE_DAY)
                .count(100)
                .adjusted(true)
                .build());

        assertTrue(lastQuery.contains("symbol=005930"));
        assertTrue(lastQuery.contains("interval=1d"));
        assertTrue(lastQuery.contains("count=100"));
        assertTrue(lastQuery.contains("adjusted=true"));
        // `before` was never set, so it must be omitted.
        assertTrue(!lastQuery.contains("before"));
    }

    @Test
    void orderQueryOmitsUnsetOptionalParams() {
        route("/api/v1/orders", 200,
                "{\"result\":{\"orders\":[],\"nextCursor\":null,\"hasNext\":false}}");
        server.start();

        TossInvestClient client = TossInvestClient.create("id", "secret", baseUrl);
        client.getOrders(42L, OrderQuery.of(OrderStatusFilter.OPEN).symbol("005930").build());

        assertTrue(lastQuery.contains("status=OPEN"));
        assertTrue(lastQuery.contains("symbol=005930"));
        assertTrue(!lastQuery.contains("cursor"));
        assertTrue(!lastQuery.contains("limit"));
    }
}
