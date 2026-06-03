package io.github.jy2694.tossinvest.model.common;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Currency code. The API may add values, so unknowns map to {@link #UNKNOWN}. */
public enum Currency {
    KRW,
    USD,
    @JsonEnumDefaultValue UNKNOWN
}
