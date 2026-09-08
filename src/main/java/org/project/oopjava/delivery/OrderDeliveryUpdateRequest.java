package org.project.oopjava.delivery;

import jakarta.validation.constraints.Size;

public record OrderDeliveryUpdateRequest(
        String status,
        @Size(max = 255)
        String location,
        @Size(max = 1000)
        String message) {
}
