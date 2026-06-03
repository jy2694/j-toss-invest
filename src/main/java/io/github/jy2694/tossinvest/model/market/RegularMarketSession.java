package io.github.jy2694.tossinvest.model.market;

import java.time.OffsetDateTime;

/** KR regular market session (union of KRX and NXT). */
public record RegularMarketSession(
        OffsetDateTime startTime,
        OffsetDateTime singlePriceAuctionStartTime,
        OffsetDateTime endTime) {
}
