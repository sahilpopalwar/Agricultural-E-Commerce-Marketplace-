package org.project.oopjava.order;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
        @NotNull @Min(1) Integer productId,
        @NotNull @Min(1) @Max(1000) Integer quantity) {
}
