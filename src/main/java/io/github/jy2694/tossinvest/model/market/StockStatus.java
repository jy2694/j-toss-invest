package io.github.jy2694.tossinvest.model.market;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Listing status of a stock. */
public enum StockStatus {
    SCHEDULED,
    ACTIVE,
    DELISTED,
    @JsonEnumDefaultValue UNKNOWN
}
