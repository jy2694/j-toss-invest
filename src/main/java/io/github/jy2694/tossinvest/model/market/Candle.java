package io.github.jy2694.tossinvest.model.market;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import io.github.jy2694.tossinvest.model.common.Currency;

/** A single OHLCV candle. */
public record Candle(
        OffsetDateTime timestamp,
        BigDecimal openPrice,
        BigDecimal highPrice,
        BigDecimal lowPrice,
        BigDecimal closePrice,
        BigDecimal volume,
        Currency currency) {
}
