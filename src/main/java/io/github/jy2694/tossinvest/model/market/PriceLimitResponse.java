package io.github.jy2694.tossinvest.model.market;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import io.github.jy2694.tossinvest.model.common.Currency;

/** Upper/lower price limits for a symbol. */
public record PriceLimitResponse(
        OffsetDateTime timestamp,
        BigDecimal upperLimitPrice,
        BigDecimal lowerLimitPrice,
        Currency currency) {
}
