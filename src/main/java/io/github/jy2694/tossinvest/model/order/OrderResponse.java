package io.github.jy2694.tossinvest.model.order;

/** Response to order creation. */
public record OrderResponse(
        String orderId,
        String clientOrderId) {
}
