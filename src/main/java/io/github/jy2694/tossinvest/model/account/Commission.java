package io.github.jy2694.tossinvest.model.account;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.github.jy2694.tossinvest.model.common.MarketCountry;

/** Trading commission rate for a market, optionally bounded by a date range. */
public record Commission(
        MarketCountry marketCountry,
        BigDecimal commissionRate,
        LocalDate startDate,
        LocalDate endDate) {
}
