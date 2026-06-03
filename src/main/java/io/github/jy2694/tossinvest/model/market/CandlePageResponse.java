package io.github.jy2694.tossinvest.model.market;

import java.time.OffsetDateTime;
import java.util.List;

/** A page of candles, with a cursor for fetching older data. */
public record CandlePageResponse(
        List<Candle> candles,
        OffsetDateTime nextBefore) {
}
