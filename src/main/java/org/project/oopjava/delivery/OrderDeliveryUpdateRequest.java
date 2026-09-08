package org.project.oopjava.delivery;

public record OrderDeliveryUpdateRequest(
        String status,
        String location,
        String message) {
}
