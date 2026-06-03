package io.github.jy2694.tossinvest.model.market;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.github.jy2694.tossinvest.model.common.Currency;

/** Basic instrument information. */
public record StockInfo(
        String symbol,
        String name,
        String englishName,
        String isinCode,
        StockMarket market,
        SecurityType securityType,
        boolean isCommonShare,
        StockStatus status,
        Currency currency,
        LocalDate listDate,
        LocalDate delistDate,
        BigDecimal sharesOutstanding,
        BigDecimal leverageFactor,
        KrMarketDetail koreanMarketDetail) {
}
