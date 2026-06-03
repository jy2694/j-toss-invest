package io.github.jy2694.tossinvest.model.account;

import java.math.BigDecimal;

/** Portfolio daily profit/loss, summed per currency across all holdings. */
public record OverviewDailyProfitLoss(
        Price amount,
        BigDecimal rate) {
}
