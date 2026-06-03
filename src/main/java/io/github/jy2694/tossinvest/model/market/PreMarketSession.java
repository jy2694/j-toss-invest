package io.github.jy2694.tossinvest.model.market;

import java.time.OffsetDateTime;

/** KR pre-market session (NXT). */
public record PreMarketSession(
        OffsetDateTime startTime,
        OffsetDateTime singlePriceAuctionStartTime,
        OffsetDateTime endTime) {
}
