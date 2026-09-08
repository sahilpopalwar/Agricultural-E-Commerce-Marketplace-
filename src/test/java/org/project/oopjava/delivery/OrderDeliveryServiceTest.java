package org.project.oopjava.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class OrderDeliveryServiceTest {
    @Mock
    private KafkaTemplate<String, OrderDeliveryEvent> kafkaTemplate;

    @Mock
    private OrderDeliveryStatusRepository statusRepository;

    @Test
    void publishesAnOrderKeyedDeliveryEvent() {
        OrderDeliveryService service =
                new OrderDeliveryService(kafkaTemplate, statusRepository, "delivery-events");

        OrderDeliveryEvent event = service.publish(
                "42",
                new OrderDeliveryUpdateRequest("in_transit", "Nanded", "On the way"));

        assertEquals("42", event.orderId());
        assertEquals(DeliveryStatus.IN_TRANSIT, event.status());
        verify(kafkaTemplate).send(eq("delivery-events"), eq("42"), eq(event));
    }

    @Test
    void consumerMaterializesEventsInTheStatusRepository() {
        OrderDeliveryService service =
                new OrderDeliveryService(kafkaTemplate, statusRepository, "delivery-events");
        OrderDeliveryEvent event = OrderDeliveryEvent.create(
                "order-42", DeliveryStatus.DELIVERED, "Nanded", "Delivered");

        service.consume(event);

        verify(statusRepository).saveLatest(event);
    }

    @Test
    void rejectsMalformedOrderIdsBeforePublishing() {
        OrderDeliveryService service =
                new OrderDeliveryService(kafkaTemplate, statusRepository, "delivery-events");

        assertThrows(IllegalArgumentException.class, () -> service.publish(
                "order/42", new OrderDeliveryUpdateRequest("delivered", null, null)));
    }
}
