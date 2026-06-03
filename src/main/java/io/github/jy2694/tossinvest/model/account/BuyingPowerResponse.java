package io.github.jy2694.tossinvest.model.account;

import java.math.BigDecimal;

import io.github.jy2694.tossinvest.model.common.Currency;

/** Cash buying power for an account in a given currency. */
public record BuyingPowerResponse(
        Currency currency,
        BigDecimal cashBuyingPower) {
}
