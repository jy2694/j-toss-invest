package io.github.jy2694.tossinvest.model.account;

import java.math.BigDecimal;

/** Per-currency amount. Each field holds only the sum of that currency (no FX conversion). */
public record Price(
        BigDecimal krw,
        BigDecimal usd) {
}
