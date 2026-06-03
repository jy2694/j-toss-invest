package io.github.jy2694.tossinvest.model.market;

import java.time.OffsetDateTime;
import java.util.List;

import io.github.jy2694.tossinvest.model.common.Currency;

/** Order book snapshot for a symbol. */
public record OrderbookResponse(
        OffsetDateTime timestamp,
        Currency currency,
        List<OrderbookEntry> asks,
        List<OrderbookEntry> bids) {
}
