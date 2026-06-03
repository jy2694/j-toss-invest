package io.github.jy2694.tossinvest.model.account;

/** Portfolio market value, summed per currency across all holdings. */
public record OverviewMarketValue(
        Price amount,
        Price amountAfterCost) {
}
