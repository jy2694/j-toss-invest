package io.github.jy2694.tossinvest.model.order;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Order direction. */
public enum OrderSide {
    BUY,
    SELL,
    @JsonEnumDefaultValue UNKNOWN
}
