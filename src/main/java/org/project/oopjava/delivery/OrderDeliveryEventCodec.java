package org.project.oopjava.delivery;

import java.time.Instant;

import org.project.oopjava.delivery.proto.DeliveryEventProto;
import com.google.protobuf.Timestamp;

final class OrderDeliveryEventCodec {
    private OrderDeliveryEventCodec() {
    }

    static byte[] toProtobuf(OrderDeliveryEvent event) {
        return DeliveryEventProto.DeliveryEvent.newBuilder()
                .setEventId(event.eventId())
                .setOrderId(event.orderId())
                .setStatus(event.status().name())
                .setLocation(event.location() == null ? "" : event.location())
                .setMessage(event.message() == null ? "" : event.message())
                .setOccurredAt(Timestamp.newBuilder()
                        .setSeconds(event.occurredAt().getEpochSecond())
                        .setNanos(event.occurredAt().getNano())
                        .build())
                .build()
                .toByteArray();
    }

    static OrderDeliveryEvent fromProtobuf(byte[] payload) {
        try {
            DeliveryEventProto.DeliveryEvent event =
                    DeliveryEventProto.DeliveryEvent.parseFrom(payload);
            return new OrderDeliveryEvent(
                    event.getEventId(),
                    event.getOrderId(),
                    DeliveryStatus.valueOf(event.getStatus()),
                    event.getLocation().isEmpty() ? null : event.getLocation(),
                    event.getMessage().isEmpty() ? null : event.getMessage(),
                    Instant.ofEpochSecond(
                            event.getOccurredAt().getSeconds(),
                            event.getOccurredAt().getNanos()));
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid Protobuf delivery event", exception);
        }
    }
}
