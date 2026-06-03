package io.github.jy2694.tossinvest.model.account;

import java.math.BigDecimal;

/** Profit/loss of a single holding, in its trading currency. */
public record ProfitLoss(
        BigDecimal amount,
        BigDecimal amountAfterCost,
        BigDecimal rate,
        BigDecimal rateAfterCost) {
}
