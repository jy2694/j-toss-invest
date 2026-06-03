package io.github.jy2694.tossinvest.model.order;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Order lifecycle status. */
public enum OrderStatus {
    PENDING,
    PENDING_CANCEL,
    PENDING_REPLACE,
    PARTIAL_FILLED,
    FILLED,
    CANCELED,
    REJECTED,
    CANCEL_REJECTED,
    REPLACE_REJECTED,
    REPLACED,
    @JsonEnumDefaultValue UNKNOWN
}
