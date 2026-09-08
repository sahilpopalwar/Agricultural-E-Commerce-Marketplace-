package org.project.oopjava.delivery;

import java.time.Instant;
import java.util.UUID;

/**
 * The Kafka contract for an order delivery status change.
 */
public record OrderDeliveryEvent(
        String eventId,
        String orderId,
        DeliveryStatus status,
        String location,
        String message,
        Instant occurredAt) {

    public OrderDeliveryEvent {
        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("eventId is required");
        }
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId is required");
        }
        if (status == null) {
            throw new IllegalArgumentException("status is required");
        }
        if (occurredAt == null) {
            throw new IllegalArgumentException("occurredAt is required");
        }
    }

    public static OrderDeliveryEvent create(
            String orderId,
            DeliveryStatus status,
            String location,
            String message) {
        return new OrderDeliveryEvent(
                UUID.randomUUID().toString(),
                orderId,
                status,
                location,
                message,
                Instant.now());
    }
}
