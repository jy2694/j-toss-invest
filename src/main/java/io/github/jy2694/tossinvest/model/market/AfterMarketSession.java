package io.github.jy2694.tossinvest.model.market;

import java.time.OffsetDateTime;

/** KR after-market session (NXT). */
public record AfterMarketSession(
        OffsetDateTime startTime,
        OffsetDateTime singlePriceAuctionEndTime,
        OffsetDateTime endTime) {
}
