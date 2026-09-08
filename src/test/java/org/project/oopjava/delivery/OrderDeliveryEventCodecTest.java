package org.project.oopjava.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

class OrderDeliveryEventCodecTest {
    @Test
    void roundTripsDeliveryEventsAsProtobuf() {
        OrderDeliveryEvent source =
                OrderDeliveryEvent.create("42", DeliveryStatus.IN_TRANSIT, "Nanded", "On the way");
        OrderDeliveryEventSerializer serializer = new OrderDeliveryEventSerializer();
        OrderDeliveryEventDeserializer deserializer = new OrderDeliveryEventDeserializer();

        serializer.configure(Map.of("kafka.value.format", "protobuf"), false);
        deserializer.configure(Map.of("kafka.value.format", "protobuf"), false);

        assertEquals(source, deserializer.deserialize(
                "delivery-events", serializer.serialize("delivery-events", source)));
    }

    @Test
    void roundTripsDeliveryEventsAsJson() {
        OrderDeliveryEvent source =
                OrderDeliveryEvent.create("42", DeliveryStatus.DELIVERED, null, "Delivered");
        OrderDeliveryEventSerializer serializer = new OrderDeliveryEventSerializer();
        OrderDeliveryEventDeserializer deserializer = new OrderDeliveryEventDeserializer();

        serializer.configure(Map.of("kafka.value.format", "json"), false);
        deserializer.configure(Map.of("kafka.value.format", "json"), false);

        assertEquals(source, deserializer.deserialize(
                "delivery-events", serializer.serialize("delivery-events", source)));
    }
}
