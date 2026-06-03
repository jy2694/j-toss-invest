package io.github.jy2694.tossinvest.model.market;

import com.fasterxml.jackson.annotation.JsonValue;

/** Candle chart interval. */
public enum CandleInterval {
    ONE_MINUTE("1m"),
    ONE_DAY("1d");

    private final String wireValue;

    CandleInterval(String wireValue) {
        this.wireValue = wireValue;
    }

    @JsonValue
    public String wireValue() {
        return wireValue;
    }
}
