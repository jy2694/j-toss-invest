package io.github.jy2694.tossinvest.model.common;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Market country. KR = KRX, US = NYSE/NASDAQ etc. Unknowns map to {@link #UNKNOWN}. */
public enum MarketCountry {
    KR,
    US,
    @JsonEnumDefaultValue UNKNOWN
}
