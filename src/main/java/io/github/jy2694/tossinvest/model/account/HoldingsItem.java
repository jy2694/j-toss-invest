package io.github.jy2694.tossinvest.model.account;

import java.math.BigDecimal;

import io.github.jy2694.tossinvest.model.common.Currency;
import io.github.jy2694.tossinvest.model.common.MarketCountry;

/** A single holding within the portfolio. */
public record HoldingsItem(
        String symbol,
        String name,
        MarketCountry marketCountry,
        Currency currency,
        BigDecimal quantity,
        BigDecimal lastPrice,
        BigDecimal averagePurchasePrice,
        MarketValue marketValue,
        ProfitLoss profitLoss,
        DailyProfitLoss dailyProfitLoss,
        Cost cost) {
}
