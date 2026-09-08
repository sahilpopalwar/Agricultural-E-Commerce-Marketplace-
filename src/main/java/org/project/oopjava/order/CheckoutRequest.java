package org.project.oopjava.order;

import java.util.List;

public record CheckoutRequest(
        List<OrderItemRequest> items,
        String shippingAddress,
        String paymentMethod) {
}
