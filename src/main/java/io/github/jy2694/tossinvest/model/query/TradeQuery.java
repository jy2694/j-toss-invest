package io.github.jy2694.tossinvest.model.query;

/**
 * Parameters for the recent-trades query. Required: {@code symbol}.
 *
 * <pre>{@code
 * TradeQuery.of("005930").count(50).build();
 * }</pre>
 */
public record TradeQuery(String symbol, Integer count) {

    /** Starts a builder with the required symbol. */
    public static Builder of(String symbol) {
        return new Builder(symbol);
    }

    public static final class Builder {
        private final String symbol;
        private Integer count;

        private Builder(String symbol) {
            this.symbol = symbol;
        }

        /** Max number of trades to return. */
        public Builder count(Integer count) {
            this.count = count;
            return this;
        }

        public TradeQuery build() {
            return new TradeQuery(symbol, count);
        }
    }
}
