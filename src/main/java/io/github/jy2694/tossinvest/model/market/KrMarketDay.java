package io.github.jy2694.tossinvest.model.market;

import java.time.LocalDate;

/** KR business-day info; {@code integrated} is {@code null} when fully closed. */
public record KrMarketDay(
        LocalDate date,
        IntegratedHour integrated) {
}
