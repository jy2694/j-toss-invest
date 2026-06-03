package io.github.jy2694.tossinvest.model.market;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Security type of an instrument. */
public enum SecurityType {
    STOCK,
    FOREIGN_STOCK,
    DEPOSITARY_RECEIPT,
    INFRASTRUCTURE_FUND,
    REIT,
    ETF,
    FOREIGN_ETF,
    ETN,
    STOCK_WARRANTS,
    @JsonEnumDefaultValue UNKNOWN
}
