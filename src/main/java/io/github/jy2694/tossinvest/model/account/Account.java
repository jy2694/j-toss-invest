package io.github.jy2694.tossinvest.model.account;

/** A brokerage account. */
public record Account(
        String accountNo,
        long accountSeq,
        AccountType accountType) {
}
