package io.github.jy2694.tossinvest.model.market;

import java.time.OffsetDateTime;

/** A US market session (day / pre / regular / after all share this shape). */
public record UsMarketSession(
        OffsetDateTime startTime,
        OffsetDateTime endTime) {
}
