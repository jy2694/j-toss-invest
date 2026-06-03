package io.github.jy2694.tossinvest.model.market;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Direction of an exchange-rate change. */
public enum RateChangeType {
    UP,
    EQUAL,
    DOWN,
    @JsonEnumDefaultValue UNKNOWN
}
