package io.github.jy2694.tossinvest.model.market;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Listing market of a stock. */
public enum StockMarket {
    KOSPI,
    KOSDAQ,
    NYSE,
    NASDAQ,
    AMEX,
    KR_ETC,
    US_ETC,
    @JsonEnumDefaultValue UNKNOWN
}
