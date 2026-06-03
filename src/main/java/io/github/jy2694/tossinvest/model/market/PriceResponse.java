package io.github.jy2694.tossinvest.model.market;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import io.github.jy2694.tossinvest.model.common.Currency;

/** Current price for a symbol. */
public record PriceResponse(
        String symbol,
        OffsetDateTime timestamp,
        BigDecimal lastPrice,
        Currency currency) {
}
