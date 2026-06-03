package io.github.jy2694.tossinvest.model.account;

import java.math.BigDecimal;

/** Cost of a single holding (commission and tax), in its trading currency. */
public record Cost(
        BigDecimal commission,
        BigDecimal tax) {
}
