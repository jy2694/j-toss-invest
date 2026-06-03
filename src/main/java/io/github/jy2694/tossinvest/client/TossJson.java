package io.github.jy2694.tossinvest.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/** Builds the {@link ObjectMapper} configured for the Toss Invest API. */
final class TossJson {

    private TossJson() {
    }

    static ObjectMapper newMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                // Tolerate new fields and enum values the client doesn't know yet.
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
                // Emit ISO-8601 timestamps, not numeric.
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}
