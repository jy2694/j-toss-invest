package io.github.jy2694.tossinvest.model.query;

import io.github.jy2694.tossinvest.model.market.CandleInterval;

/**
 * Parameters for the candle chart query. Required: {@code symbol}, {@code interval}.
 *
 * <pre>{@code
 * CandleQuery.of("005930", CandleInterval.ONE_DAY)
 *         .count(100)
 *         .adjusted(true)
 *         .build();
 * }</pre>
 */
public record CandleQuery(
        String symbol,
        CandleInterval interval,
        Integer count,
        String before,
        Boolean adjusted) {

    /** Starts a builder with the two required parameters. */
    public static Builder of(String symbol, CandleInterval interval) {
        return new Builder(symbol, interval);
    }

    public static final class Builder {
        private final String symbol;
        private final CandleInterval interval;
        private Integer count;
        private String before;
        private Boolean adjusted;

        private Builder(String symbol, CandleInterval interval) {
            this.symbol = symbol;
            this.interval = interval;
        }

        /** Max number of candles to return. */
        public Builder count(Integer count) {
            this.count = count;
            return this;
        }

        /** Cursor: return candles before this timestamp. */
        public Builder before(String before) {
            this.before = before;
            return this;
        }

        /** Whether to return split/dividend-adjusted prices. */
        public Builder adjusted(Boolean adjusted) {
            this.adjusted = adjusted;
            return this;
        }

        public CandleQuery build() {
            return new CandleQuery(symbol, interval, count, before, adjusted);
        }
    }
}
