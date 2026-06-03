package io.github.jy2694.tossinvest.model.market;

/** KR market calendar: today plus the adjacent business days. */
public record KrMarketCalendarResponse(
        KrMarketDay today,
        KrMarketDay previousBusinessDay,
        KrMarketDay nextBusinessDay) {
}
