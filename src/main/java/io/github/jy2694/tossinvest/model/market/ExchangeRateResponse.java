package io.github.jy2694.tossinvest.model.market;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import io.github.jy2694.tossinvest.model.common.Currency;

/** Exchange rate between two currencies. */
public record ExchangeRateResponse(
        Currency baseCurrency,
        Currency quoteCurrency,
        BigDecimal rate,
        BigDecimal midRate,
        BigDecimal basisPoint,
        RateChangeType rateChangeType,
        OffsetDateTime validFrom,
        OffsetDateTime validUntil) {
}
