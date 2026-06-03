package io.github.jy2694.tossinvest.model.account;

import java.math.BigDecimal;

/** Portfolio profit/loss, summed per currency across all holdings. */
public record OverviewProfitLoss(
        Price amount,
        Price amountAfterCost,
        BigDecimal rate,
        BigDecimal rateAfterCost) {
}
