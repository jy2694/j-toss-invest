package io.github.jy2694.tossinvest.model.account;

import java.math.BigDecimal;

/** Daily profit/loss of a single holding, in its trading currency. */
public record DailyProfitLoss(
        BigDecimal amount,
        BigDecimal rate) {
}
