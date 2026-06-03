package io.github.jy2694.tossinvest.model.account;

import java.math.BigDecimal;

/** Sellable quantity of a symbol in an account. */
public record SellableQuantityResponse(BigDecimal sellableQuantity) {
}
