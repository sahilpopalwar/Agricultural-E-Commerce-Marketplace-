package org.project.oopjava.order;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CheckoutRequest(
        @NotEmpty List<@Valid OrderItemRequest> items,
        @NotBlank @Size(max = 2000)
        String shippingAddress,
        @NotBlank @Size(max = 50)
        String paymentMethod) {
}
