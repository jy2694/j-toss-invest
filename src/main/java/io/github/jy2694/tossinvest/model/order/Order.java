package io.github.jy2694.tossinvest.model.order;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import io.github.jy2694.tossinvest.model.common.Currency;

/** A full order with its current status and execution detail. */
public record Order(
        String orderId,
        String symbol,
        OrderSide side,
        OrderType orderType,
        TimeInForce timeInForce,
        OrderStatus status,
        BigDecimal price,
        BigDecimal quantity,
        BigDecimal orderAmount,
        Currency currency,
        OffsetDateTime orderedAt,
        OffsetDateTime canceledAt,
        OrderExecution execution) {
}
