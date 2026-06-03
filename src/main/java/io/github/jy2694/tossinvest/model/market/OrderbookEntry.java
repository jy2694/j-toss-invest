package io.github.jy2694.tossinvest.model.market;

import java.math.BigDecimal;

/** A single price level in the order book. */
public record OrderbookEntry(BigDecimal price, BigDecimal volume) {
}
