package io.github.jy2694.tossinvest.model.market;

import java.time.LocalDate;

/** A buy-caution warning attached to a stock. */
public record StockWarning(
        StockWarningType warningType,
        String exchange,
        LocalDate startDate,
        LocalDate endDate) {
}
