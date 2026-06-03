package io.github.jy2694.tossinvest.model.order;

import java.util.List;

/**
 * Paginated order list. For {@code status=OPEN}, all pending orders are returned
 * and {@code nextCursor} is always {@code null} with {@code hasNext=false}.
 */
public record PaginatedOrderResponse(
        List<Order> orders,
        String nextCursor,
        boolean hasNext) {
}
