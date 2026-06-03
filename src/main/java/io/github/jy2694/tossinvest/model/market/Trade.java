package io.github.jy2694.tossinvest.model.market;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import io.github.jy2694.tossinvest.model.common.Currency;

/** A single recent trade execution. */
public record Trade(
        BigDecimal price,
        BigDecimal volume,
        OffsetDateTime timestamp,
        Currency currency) {
}
