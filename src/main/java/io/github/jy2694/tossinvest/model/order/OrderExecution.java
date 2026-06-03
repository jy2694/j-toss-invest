package io.github.jy2694.tossinvest.model.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/** Execution (fill) detail for an order. */
public record OrderExecution(
        BigDecimal filledQuantity,
        BigDecimal averageFilledPrice,
        BigDecimal filledAmount,
        BigDecimal commission,
        BigDecimal tax,
        OffsetDateTime filledAt,
        LocalDate settlementDate) {
}
