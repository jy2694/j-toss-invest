package io.github.jy2694.tossinvest.model.order;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Order price type: LIMIT (지정가) or MARKET (시장가). */
public enum OrderType {
    LIMIT,
    MARKET,
    @JsonEnumDefaultValue UNKNOWN
}
