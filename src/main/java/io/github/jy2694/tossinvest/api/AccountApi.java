package io.github.jy2694.tossinvest.api;

import java.util.List;

import io.github.jy2694.tossinvest.exception.TossApiStatusException;
import io.github.jy2694.tossinvest.http.TossRequest;
import io.github.jy2694.tossinvest.model.account.Account;
import io.github.jy2694.tossinvest.model.account.BuyingPowerResponse;
import io.github.jy2694.tossinvest.model.account.Commission;
import io.github.jy2694.tossinvest.model.account.HoldingsOverview;
import io.github.jy2694.tossinvest.model.account.SellableQuantityResponse;
import io.github.jy2694.tossinvest.model.common.Currency;

/**
 * Account endpoints: account list, holdings, buying power, sellable quantity,
 * and commissions.
 *
 * <p>Every method except {@link #getAccounts()} is account-scoped: pass the
 * {@code accountId}, which is the {@link Account#accountSeq()} from
 * {@link #getAccounts()}. It is sent as the {@value #ACCOUNT_HEADER} header.
 * On a non-2xx response the call throws a {@link TossApiStatusException}.
 */
public interface AccountApi extends TossApi {

    /** Header that scopes a request to a specific account. */
    String ACCOUNT_HEADER = "X-Tossinvest-Account";

    /**
     * 계좌 목록 조회 — lists the caller's accounts.
     *
     * @return the accounts; empty if there are none
     * @throws TossApiStatusException on a non-2xx response
     */
    default List<Account> getAccounts() {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/accounts")
                .build();
        return executeForList(request, Account.class);
    }

    /**
     * 보유 주식 조회 — fetches the holdings overview for an account.
     *
     * @param accountId account sequence ({@link Account#accountSeq()})
     * @param symbol restrict to one symbol, or {@code null} for all holdings
     * @return the holdings overview with per-symbol breakdown
     * @throws TossApiStatusException on a non-2xx response
     */
    default HoldingsOverview getHoldings(long accountId, String symbol) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/holdings")
                .header(ACCOUNT_HEADER, Long.toString(accountId))
                .query("symbol", symbol)
                .build();
        return executeForObject(request, HoldingsOverview.class);
    }

    /**
     * 매수 가능 금액 조회 — fetches cash buying power for a currency.
     *
     * @param accountId account sequence ({@link Account#accountSeq()})
     * @param currency currency to report buying power in
     * @return the cash buying power
     * @throws TossApiStatusException on a non-2xx response
     */
    default BuyingPowerResponse getBuyingPower(long accountId, Currency currency) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/buying-power")
                .header(ACCOUNT_HEADER, Long.toString(accountId))
                .query("currency", currency.name())
                .build();
        return executeForObject(request, BuyingPowerResponse.class);
    }

    /**
     * 판매 가능 수량 조회 — fetches the sellable quantity of a symbol.
     *
     * @param accountId account sequence ({@link Account#accountSeq()})
     * @param symbol instrument symbol
     * @return the sellable quantity
     * @throws TossApiStatusException on a non-2xx response
     */
    default SellableQuantityResponse getSellableQuantity(long accountId, String symbol) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/sellable-quantity")
                .header(ACCOUNT_HEADER, Long.toString(accountId))
                .query("symbol", symbol)
                .build();
        return executeForObject(request, SellableQuantityResponse.class);
    }

    /**
     * 매매 수수료 조회 — fetches commission rates for the account.
     *
     * @param accountId account sequence ({@link Account#accountSeq()})
     * @return commission rates per market
     * @throws TossApiStatusException on a non-2xx response
     */
    default List<Commission> getCommissions(long accountId) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/commissions")
                .header(ACCOUNT_HEADER, Long.toString(accountId))
                .build();
        return executeForList(request, Commission.class);
    }
}
