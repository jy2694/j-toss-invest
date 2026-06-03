package io.github.jy2694.tossinvest.model.market;

/** US market calendar: today plus the adjacent business days. */
public record UsMarketCalendarResponse(
        UsMarketDay today,
        UsMarketDay previousBusinessDay,
        UsMarketDay nextBusinessDay) {
}
