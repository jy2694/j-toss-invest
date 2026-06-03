package io.github.jy2694.tossinvest.model.account;

import java.math.BigDecimal;

/** Market value of a single holding, in its trading currency. */
public record MarketValue(
        BigDecimal purchaseAmount,
        BigDecimal amount,
        BigDecimal amountAfterCost) {
}
