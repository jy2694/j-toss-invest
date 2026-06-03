package io.github.jy2694.tossinvest;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import io.github.jy2694.tossinvest.client.TossInvestClient;
import io.github.jy2694.tossinvest.model.common.Currency;
import io.github.jy2694.tossinvest.model.market.CandleInterval;
import io.github.jy2694.tossinvest.model.order.OrderCreateRequest;
import io.github.jy2694.tossinvest.model.order.OrderModifyRequest;
import io.github.jy2694.tossinvest.model.order.OrderSide;
import io.github.jy2694.tossinvest.model.order.OrderType;
import io.github.jy2694.tossinvest.model.order.TimeInForce;
import io.github.jy2694.tossinvest.model.query.CandleQuery;
import io.github.jy2694.tossinvest.model.query.ExchangeRateQuery;
import io.github.jy2694.tossinvest.model.query.OrderQuery;
import io.github.jy2694.tossinvest.model.query.OrderStatusFilter;
import io.github.jy2694.tossinvest.model.query.TradeQuery;

/**
 * Compile-only guard: every call shape shown in the README must keep compiling.
 * The client is never invoked, so no network is touched.
 */
class ReadmeExamplesTest {

    @Test
    @SuppressWarnings("unused")
    void readmeSnippetsCompile() {
        TossInvestClient client = TossInvestClient.create("id", "secret");
        long accountSeq = 1L;

        Runnable marketData = () -> {
            client.getOrderbook("005930");
            client.getPrices("005930,AAPL");
            client.getTrades(TradeQuery.of("005930").count(10).build());
            client.getPriceLimit("005930");
            client.getCandles(CandleQuery.of("005930", CandleInterval.ONE_DAY).count(20).build());
            client.getStocks("005930,AAPL");
            client.getStockWarnings("005930");
            client.getExchangeRate(ExchangeRateQuery.of(Currency.USD, Currency.KRW).build());
            client.getKrMarketCalendar(null);
            client.getUsMarketCalendar(null);
        };

        Runnable account = () -> {
            client.getAccounts();
            client.getHoldings(accountSeq, null);
            client.getBuyingPower(accountSeq, Currency.KRW);
            client.getSellableQuantity(accountSeq, "005930");
            client.getCommissions(accountSeq);
        };

        Runnable orders = () -> {
            client.createOrder(accountSeq,
                    OrderCreateRequest.limit("005930", OrderSide.BUY,
                            new BigDecimal("10"), new BigDecimal("70000")));
            client.createOrder(accountSeq,
                    OrderCreateRequest.amountBased("AAPL", OrderSide.BUY, new BigDecimal("100.00")));
            client.createOrder(accountSeq,
                    OrderCreateRequest.limit("AAPL", OrderSide.BUY,
                                    new BigDecimal("5"), new BigDecimal("185.50"))
                            .toBuilder()
                            .timeInForce(TimeInForce.CLS)
                            .clientOrderId("my-order-001")
                            .build());
            client.modifyOrder(accountSeq, "order-1",
                    OrderModifyRequest.of(OrderType.LIMIT)
                            .price(new BigDecimal("71000"))
                            .quantity(new BigDecimal("15"))
                            .build());
            client.cancelOrder(accountSeq, "order-1");
            client.getOrders(accountSeq, OrderQuery.of(OrderStatusFilter.OPEN).build());
            client.getOrder(accountSeq, "order-1");
        };
    }
}
