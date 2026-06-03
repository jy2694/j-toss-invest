package io.github.jy2694.tossinvest.model.query;

/**
 * Parameters for the order list query. Required: {@code status}.
 *
 * <pre>{@code
 * OrderQuery.of(OrderStatusFilter.OPEN).symbol("005930").limit(50).build();
 * }</pre>
 */
public record OrderQuery(
        OrderStatusFilter status,
        String symbol,
        String from,
        String to,
        String cursor,
        Integer limit) {

    /** Starts a builder with the required status filter. */
    public static Builder of(OrderStatusFilter status) {
        return new Builder(status);
    }

    public static final class Builder {
        private final OrderStatusFilter status;
        private String symbol;
        private String from;
        private String to;
        private String cursor;
        private Integer limit;

        private Builder(OrderStatusFilter status) {
            this.status = status;
        }

        /** Filter by symbol. */
        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        /** Start of the time range (inclusive). */
        public Builder from(String from) {
            this.from = from;
            return this;
        }

        /** End of the time range (inclusive). */
        public Builder to(String to) {
            this.to = to;
            return this;
        }

        /** Pagination cursor from a previous page. */
        public Builder cursor(String cursor) {
            this.cursor = cursor;
            return this;
        }

        /** Max number of orders per page. */
        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public OrderQuery build() {
            return new OrderQuery(status, symbol, from, to, cursor, limit);
        }
    }
}
