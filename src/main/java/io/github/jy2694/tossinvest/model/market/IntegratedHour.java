package io.github.jy2694.tossinvest.model.market;

/** KR integrated (KRX+NXT) trading hours; any session may be {@code null} when closed. */
public record IntegratedHour(
        PreMarketSession preMarket,
        RegularMarketSession regularMarket,
        AfterMarketSession afterMarket) {
}
