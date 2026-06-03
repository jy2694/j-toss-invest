package io.github.jy2694.tossinvest.model.account;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Type of a brokerage account. */
public enum AccountType {
    BROKERAGE,
    OVERSEAS_DERIVATIVES,
    PENSION_SAVINGS,
    RESHORING_INVESTMENT,
    @JsonEnumDefaultValue UNKNOWN
}
