package io.github.jy2694.tossinvest.model.market;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Type of a buy-caution warning on a stock. */
public enum StockWarningType {
    LIQUIDATION_TRADING,
    OVERHEATED,
    INVESTMENT_WARNING,
    INVESTMENT_RISK,
    VI_STATIC_AND_DYNAMIC,
    VI_STATIC,
    VI_DYNAMIC,
    STOCK_WARRANTS,
    @JsonEnumDefaultValue UNKNOWN
}
