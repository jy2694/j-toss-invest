package io.github.jy2694.tossinvest.model.market;

/** Korean-market-specific detail, present only for KR stocks. */
public record KrMarketDetail(
        boolean liquidationTrading,
        boolean nxtSupported,
        boolean krxTradingSuspended,
        Boolean nxtTradingSuspended) {
}
