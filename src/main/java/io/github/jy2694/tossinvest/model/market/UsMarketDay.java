package io.github.jy2694.tossinvest.model.market;

import java.time.LocalDate;

/** US business-day info; each session is {@code null} when that session is closed. */
public record UsMarketDay(
        LocalDate date,
        UsMarketSession dayMarket,
        UsMarketSession preMarket,
        UsMarketSession regularMarket,
        UsMarketSession afterMarket) {
}
