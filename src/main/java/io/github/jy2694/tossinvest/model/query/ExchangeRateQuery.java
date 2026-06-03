package io.github.jy2694.tossinvest.model.query;

import io.github.jy2694.tossinvest.model.common.Currency;

/**
 * Parameters for the exchange-rate query. Required: {@code baseCurrency},
 * {@code quoteCurrency}.
 *
 * <pre>{@code
 * ExchangeRateQuery.of(Currency.USD, Currency.KRW).dateTime("2026-03-25T10:00:00+09:00").build();
 * }</pre>
 */
public record ExchangeRateQuery(
        Currency baseCurrency,
        Currency quoteCurrency,
        String dateTime) {

    /** Starts a builder with the required currency pair. */
    public static Builder of(Currency baseCurrency, Currency quoteCurrency) {
        return new Builder(baseCurrency, quoteCurrency);
    }

    public static final class Builder {
        private final Currency baseCurrency;
        private final Currency quoteCurrency;
        private String dateTime;

        private Builder(Currency baseCurrency, Currency quoteCurrency) {
            this.baseCurrency = baseCurrency;
            this.quoteCurrency = quoteCurrency;
        }

        /** Point in time for the rate; omit for the latest. */
        public Builder dateTime(String dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public ExchangeRateQuery build() {
            return new ExchangeRateQuery(baseCurrency, quoteCurrency, dateTime);
        }
    }
}
