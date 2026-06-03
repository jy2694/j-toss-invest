package io.github.jy2694.tossinvest.model.order;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request to modify an existing order. Required: {@code orderType}. Null fields
 * are omitted.
 *
 * <pre>{@code
 * OrderModifyRequest.of(OrderType.LIMIT).price(new BigDecimal("71000")).build();
 * }</pre>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderModifyRequest(
        OrderType orderType,
        BigDecimal quantity,
        BigDecimal price,
        Boolean confirmHighValueOrder) {

    /** Starts a builder with the required order type. */
    public static Builder of(OrderType orderType) {
        return new Builder(orderType);
    }

    public static final class Builder {
        private final OrderType orderType;
        private BigDecimal quantity;
        private BigDecimal price;
        private Boolean confirmHighValueOrder;

        private Builder(OrderType orderType) {
            this.orderType = orderType;
        }

        /** New quantity. */
        public Builder quantity(BigDecimal quantity) {
            this.quantity = quantity;
            return this;
        }

        /** New price (LIMIT only). */
        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        /** Acknowledge orders of 100M KRW or more. */
        public Builder confirmHighValueOrder(Boolean confirmHighValueOrder) {
            this.confirmHighValueOrder = confirmHighValueOrder;
            return this;
        }

        public OrderModifyRequest build() {
            return new OrderModifyRequest(orderType, quantity, price, confirmHighValueOrder);
        }
    }
}
