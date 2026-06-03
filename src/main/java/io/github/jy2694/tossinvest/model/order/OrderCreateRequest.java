package io.github.jy2694.tossinvest.model.order;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request to create an order. The API accepts either a quantity-based or an
 * amount-based order. Build one with the simple factories ({@link #limit},
 * {@link #market}, {@link #amountBased}) and, to set optional fields such as
 * {@code clientOrderId} or {@code timeInForce}, chain {@code toBuilder()}:
 *
 * <pre>{@code
 * OrderCreateRequest.limit("005930", OrderSide.BUY, new BigDecimal("10"), new BigDecimal("70000"))
 *         .toBuilder()
 *         .clientOrderId("my-order-001")
 *         .timeInForce(TimeInForce.DAY)
 *         .build();
 * }</pre>
 *
 * Null fields are omitted from the JSON body.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public sealed interface OrderCreateRequest {

    String clientOrderId();

    String symbol();

    OrderSide side();

    OrderType orderType();

    Boolean confirmHighValueOrder();

    /** Quantity-based order. {@code price} is required for LIMIT, forbidden for MARKET. */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record QuantityBased(
            String clientOrderId,
            String symbol,
            OrderSide side,
            OrderType orderType,
            TimeInForce timeInForce,
            BigDecimal quantity,
            BigDecimal price,
            Boolean confirmHighValueOrder) implements OrderCreateRequest {

        /** Returns a builder seeded with this request's values. */
        public Builder toBuilder() {
            return new Builder(this);
        }

        public static final class Builder {
            private String clientOrderId;
            private final String symbol;
            private final OrderSide side;
            private final OrderType orderType;
            private TimeInForce timeInForce;
            private final BigDecimal quantity;
            private final BigDecimal price;
            private Boolean confirmHighValueOrder;

            private Builder(QuantityBased order) {
                this.clientOrderId = order.clientOrderId;
                this.symbol = order.symbol;
                this.side = order.side;
                this.orderType = order.orderType;
                this.timeInForce = order.timeInForce;
                this.quantity = order.quantity;
                this.price = order.price;
                this.confirmHighValueOrder = order.confirmHighValueOrder;
            }

            /** Idempotency key (max 36 chars, alphanumeric plus {@code -}/{@code _}). */
            public Builder clientOrderId(String clientOrderId) {
                this.clientOrderId = clientOrderId;
                return this;
            }

            /** Time-in-force; defaults to {@code DAY} when omitted. */
            public Builder timeInForce(TimeInForce timeInForce) {
                this.timeInForce = timeInForce;
                return this;
            }

            /** Acknowledge orders of 100M KRW or more. */
            public Builder confirmHighValueOrder(Boolean confirmHighValueOrder) {
                this.confirmHighValueOrder = confirmHighValueOrder;
                return this;
            }

            public QuantityBased build() {
                return new QuantityBased(clientOrderId, symbol, side, orderType,
                        timeInForce, quantity, price, confirmHighValueOrder);
            }
        }
    }

    /** Amount-based (notional) MARKET order. */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record AmountBased(
            String clientOrderId,
            String symbol,
            OrderSide side,
            OrderType orderType,
            BigDecimal orderAmount,
            Boolean confirmHighValueOrder) implements OrderCreateRequest {

        /** Returns a builder seeded with this request's values. */
        public Builder toBuilder() {
            return new Builder(this);
        }

        public static final class Builder {
            private String clientOrderId;
            private final String symbol;
            private final OrderSide side;
            private final OrderType orderType;
            private final BigDecimal orderAmount;
            private Boolean confirmHighValueOrder;

            private Builder(AmountBased order) {
                this.clientOrderId = order.clientOrderId;
                this.symbol = order.symbol;
                this.side = order.side;
                this.orderType = order.orderType;
                this.orderAmount = order.orderAmount;
                this.confirmHighValueOrder = order.confirmHighValueOrder;
            }

            /** Idempotency key (max 36 chars, alphanumeric plus {@code -}/{@code _}). */
            public Builder clientOrderId(String clientOrderId) {
                this.clientOrderId = clientOrderId;
                return this;
            }

            /** Acknowledge orders of 100M KRW or more. */
            public Builder confirmHighValueOrder(Boolean confirmHighValueOrder) {
                this.confirmHighValueOrder = confirmHighValueOrder;
                return this;
            }

            public AmountBased build() {
                return new AmountBased(clientOrderId, symbol, side, orderType,
                        orderAmount, confirmHighValueOrder);
            }
        }
    }

    /** Builds a quantity-based limit order. */
    static QuantityBased limit(String symbol, OrderSide side, BigDecimal quantity, BigDecimal price) {
        return new QuantityBased(null, symbol, side, OrderType.LIMIT, null, quantity, price, null);
    }

    /** Builds a quantity-based market order. */
    static QuantityBased market(String symbol, OrderSide side, BigDecimal quantity) {
        return new QuantityBased(null, symbol, side, OrderType.MARKET, null, quantity, null, null);
    }

    /** Builds an amount-based (notional) market order. */
    static AmountBased amountBased(String symbol, OrderSide side, BigDecimal orderAmount) {
        return new AmountBased(null, symbol, side, OrderType.MARKET, orderAmount, null);
    }
}
