package io.github.jy2694.tossinvest.model.account;

import java.util.List;

/** Portfolio overview with per-holding breakdown. */
public record HoldingsOverview(
        Price totalPurchaseAmount,
        OverviewMarketValue marketValue,
        OverviewProfitLoss profitLoss,
        OverviewDailyProfitLoss dailyProfitLoss,
        List<HoldingsItem> items) {
}
