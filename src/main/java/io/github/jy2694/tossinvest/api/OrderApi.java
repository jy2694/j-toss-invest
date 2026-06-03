package io.github.jy2694.tossinvest.api;

import static io.github.jy2694.tossinvest.api.TossApi.toStringOrNull;

import io.github.jy2694.tossinvest.exception.TossApiStatusException;
import io.github.jy2694.tossinvest.http.HttpMethod;
import io.github.jy2694.tossinvest.http.TossRequest;
import io.github.jy2694.tossinvest.model.account.Account;
import io.github.jy2694.tossinvest.model.order.Order;
import io.github.jy2694.tossinvest.model.order.OrderCreateRequest;
import io.github.jy2694.tossinvest.model.order.OrderModifyRequest;
import io.github.jy2694.tossinvest.model.order.OrderOperationResponse;
import io.github.jy2694.tossinvest.model.order.OrderResponse;
import io.github.jy2694.tossinvest.model.order.PaginatedOrderResponse;
import io.github.jy2694.tossinvest.model.query.OrderQuery;

/**
 * Order endpoints: list, create, get, modify, and cancel.
 *
 * <p>All methods are account-scoped: pass the {@code accountId}, which is the
 * {@link Account#accountSeq()} from {@link AccountApi#getAccounts()}. On a
 * non-2xx response the call throws a {@link TossApiStatusException}.
 */
public interface OrderApi extends TossApi {

    /**
     * 주문 목록 조회 — lists orders for an account.
     *
     * @param accountId account sequence ({@link Account#accountSeq()})
     * @param query status filter (required), plus optional symbol, date range,
     *              cursor, and limit
     * @return a page of orders; for {@code OPEN} all pending orders are returned
     *         with no further pages
     * @throws TossApiStatusException on a non-2xx response
     */
    default PaginatedOrderResponse getOrders(long accountId, OrderQuery query) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/orders")
                .header(AccountApi.ACCOUNT_HEADER, Long.toString(accountId))
                .query("status", query.status().name())
                .query("symbol", query.symbol())
                .query("from", query.from())
                .query("to", query.to())
                .query("cursor", query.cursor())
                .query("limit", toStringOrNull(query.limit()))
                .build();
        return executeForObject(request, PaginatedOrderResponse.class);
    }

    /**
     * 주문 생성 — creates an order.
     *
     * @param accountId account sequence ({@link Account#accountSeq()})
     * @param order the order to place; build with the factories on
     *              {@link OrderCreateRequest}
     * @return the created order's id (and {@code clientOrderId} if supplied)
     * @throws TossApiStatusException on a non-2xx response
     */
    default OrderResponse createOrder(long accountId, OrderCreateRequest order) {
        TossRequest request = TossRequest.builder()
                .method(HttpMethod.POST)
                .path("/api/v1/orders")
                .header(AccountApi.ACCOUNT_HEADER, Long.toString(accountId))
                .header("Content-Type", "application/json")
                .body(order)
                .build();
        return executeForObject(request, OrderResponse.class);
    }

    /**
     * 주문 상세 조회 — fetches a single order.
     *
     * @param accountId account sequence ({@link Account#accountSeq()})
     * @param orderId the order id
     * @return the order with its current status and execution detail
     * @throws TossApiStatusException on a non-2xx response
     */
    default Order getOrder(long accountId, String orderId) {
        TossRequest request = TossRequest.builder()
                .path("/api/v1/orders/" + orderId)
                .header(AccountApi.ACCOUNT_HEADER, Long.toString(accountId))
                .build();
        return executeForObject(request, Order.class);
    }

    /**
     * 주문 정정 — modifies an existing order.
     *
     * @param accountId account sequence ({@link Account#accountSeq()})
     * @param orderId the order id
     * @param modification the new order type and fields; build with
     *                     {@link OrderModifyRequest#of(io.github.jy2694.tossinvest.model.order.OrderType)}
     * @return the affected order id
     * @throws TossApiStatusException on a non-2xx response
     */
    default OrderOperationResponse modifyOrder(long accountId, String orderId,
                                               OrderModifyRequest modification) {
        TossRequest request = TossRequest.builder()
                .method(HttpMethod.POST)
                .path("/api/v1/orders/" + orderId + "/modify")
                .header(AccountApi.ACCOUNT_HEADER, Long.toString(accountId))
                .header("Content-Type", "application/json")
                .body(modification)
                .build();
        return executeForObject(request, OrderOperationResponse.class);
    }

    /**
     * 주문 취소 — cancels an order.
     *
     * @param accountId account sequence ({@link Account#accountSeq()})
     * @param orderId the order id
     * @return the affected order id
     * @throws TossApiStatusException on a non-2xx response
     */
    default OrderOperationResponse cancelOrder(long accountId, String orderId) {
        TossRequest request = TossRequest.builder()
                .method(HttpMethod.POST)
                .path("/api/v1/orders/" + orderId + "/cancel")
                .header(AccountApi.ACCOUNT_HEADER, Long.toString(accountId))
                .build();
        return executeForObject(request, OrderOperationResponse.class);
    }
}
