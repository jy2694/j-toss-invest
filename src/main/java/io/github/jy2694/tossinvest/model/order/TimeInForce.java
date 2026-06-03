package io.github.jy2694.tossinvest.model.order;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * Time-in-force condition. Combined with {@link OrderType} to determine the
 * order method (e.g. LIMIT + CLS = LOC).
 */
public enum TimeInForce {
    DAY,
    CLS,
    OPG,
    @JsonEnumDefaultValue UNKNOWN
}
