package io.github.jy2694.tossinvest.api;

import static io.github.jy2694.tossinvest.api.TossApi.toStringOrNull;

import java.util.List;

import io.github.jy2694.tossinvest.exception.TossApiStatusException;
import io.github.jy2694.tossinvest.http.TossRequest;
import io.github.jy2694.tossinvest.model.market.CandlePageResponse;
import io.github.jy2694.tossinvest.model.market.ExchangeRateResponse;
import io.github.jy2694.tossinvest.model.market.KrMarketCalendarResponse;
import io.github.jy2694.tossinvest.model.market.OrderbookResponse;
import io.github.jy2694.tossinvest.model.market.PriceLimitResponse;
import io.github.jy2694.tossinvest.model.market.PriceResponse;
import io.github.jy2694.tossinvest.model.market.StockInfo;
import io.github.jy2694.tossinvest.model.market.StockWarning;
import io.github.jy2694.tossinvest.model.market.Trade;
import io.github.jy2694.tossinvest.model.market.UsMarketCalendarResponse;
import io.github.jy2694.tossinvest.model.query.CandleQuery;
import io.github.jy2694.tossinvest.model.query.ExchangeRateQuery;
import io.github.jy2694.tossinvest.model.query.TradeQuery;

/**
 * Market data endpoints: order book, prices, trades, price limits, candles,
 * stock info, warnings, exchange rate, and market calendars.
 *
 * <p>Every method requires a valid access token; obtain one via
 * {@link AuthenticationApi#issueToken(String, String)} (the
 * {@link io.github.jy2694.tossinvest.client.TossInvestClient} attaches it
 * automatically). On a non-2xx response the call throws a
 * {@link TossApiStatusException}.
 */
public interface MarketDataApi extends TossApi {

    /**
     * 호가 조회 — fetches the order book (bids and asks) for a symbol.
     *
     * @param symbol instrument symbol (KRX: 6-digit code, US: ticker)
     * @return the current order book snapshot
     * @throws TossApiStatusException on a non-2xx response
     */
    default OrderbookResponse getOrderbook(String symbol) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/orderbook")
                .query("symbol", symbol)
                .build();
        return executeForObject(request, OrderbookResponse.class);
    }

    /**
     * 현재가 조회 — fetches the current price for one or more symbols.
     *
     * @param symbols comma-separated symbols (up to 200)
     * @return the current price per symbol; empty if none match
     * @throws TossApiStatusException on a non-2xx response
     */
    default List<PriceResponse> getPrices(String symbols) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/prices")
                .query("symbols", symbols)
                .build();
        return executeForList(request, PriceResponse.class);
    }

    /**
     * 최근 체결 내역 조회 — fetches recent trade executions for a symbol.
     *
     * @param query symbol (required) and optional {@code count}
     * @return recent trades, most recent first
     * @throws TossApiStatusException on a non-2xx response
     */
    default List<Trade> getTrades(TradeQuery query) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/trades")
                .query("symbol", query.symbol())
                .query("count", toStringOrNull(query.count()))
                .build();
        return executeForList(request, Trade.class);
    }

    /**
     * 상/하한가 조회 — fetches the upper/lower price limits for a symbol.
     *
     * @param symbol instrument symbol
     * @return the price limits; {@code upperLimitPrice}/{@code lowerLimitPrice} may be {@code null}
     * @throws TossApiStatusException on a non-2xx response
     */
    default PriceLimitResponse getPriceLimit(String symbol) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/price-limits")
                .query("symbol", symbol)
                .build();
        return executeForObject(request, PriceLimitResponse.class);
    }

    /**
     * 캔들 차트 조회 — fetches a page of OHLCV candles for a symbol.
     *
     * @param query symbol and interval (required), plus optional {@code count},
     *              {@code before} cursor, and {@code adjusted} flag
     * @return a page of candles; use {@code nextBefore} as the next {@code before} cursor
     * @throws TossApiStatusException on a non-2xx response
     */
    default CandlePageResponse getCandles(CandleQuery query) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/candles")
                .query("symbol", query.symbol())
                .query("interval", query.interval().wireValue())
                .query("count", toStringOrNull(query.count()))
                .query("before", query.before())
                .query("adjusted", toStringOrNull(query.adjusted()))
                .build();
        return executeForObject(request, CandlePageResponse.class);
    }

    /**
     * 종목 기본 정보 조회 — fetches basic instrument info for one or more symbols.
     *
     * @param symbols comma-separated symbols (up to 200)
     * @return info per symbol; empty if none match
     * @throws TossApiStatusException on a non-2xx response
     */
    default List<StockInfo> getStocks(String symbols) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/stocks")
                .query("symbols", symbols)
                .build();
        return executeForList(request, StockInfo.class);
    }

    /**
     * 매수 유의사항 조회 — fetches active buy-caution warnings for a symbol.
     *
     * @param symbol instrument symbol
     * @return active warnings; empty if there are none
     * @throws TossApiStatusException on a non-2xx response
     */
    default List<StockWarning> getStockWarnings(String symbol) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/stocks/" + symbol + "/warnings")
                .build();
        return executeForList(request, StockWarning.class);
    }

    /**
     * 환율 조회 — fetches the exchange rate for a currency pair.
     *
     * @param query base and quote currency (required), plus optional {@code dateTime}
     * @return the rate and its validity window
     * @throws TossApiStatusException on a non-2xx response
     */
    default ExchangeRateResponse getExchangeRate(ExchangeRateQuery query) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/exchange-rate")
                .query("baseCurrency", query.baseCurrency().name())
                .query("quoteCurrency", query.quoteCurrency().name())
                .query("dateTime", query.dateTime())
                .build();
        return executeForObject(request, ExchangeRateResponse.class);
    }

    /**
     * 국내 장 운영 정보 조회 — fetches the KR market calendar.
     *
     * @param date target date ({@code yyyy-MM-dd}); {@code null} for today
     * @return today plus the adjacent business days
     * @throws TossApiStatusException on a non-2xx response
     */
    default KrMarketCalendarResponse getKrMarketCalendar(String date) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/market-calendar/KR")
                .query("date", date)
                .build();
        return executeForObject(request, KrMarketCalendarResponse.class);
    }

    /**
     * 해외 장 운영 정보 조회 — fetches the US market calendar.
     *
     * @param date target date ({@code yyyy-MM-dd}); {@code null} for today
     * @return today plus the adjacent business days
     * @throws TossApiStatusException on a non-2xx response
     */
    default UsMarketCalendarResponse getUsMarketCalendar(String date) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/market-calendar/US")
                .query("date", date)
                .build();
        return executeForObject(request, UsMarketCalendarResponse.class);
    }
}
