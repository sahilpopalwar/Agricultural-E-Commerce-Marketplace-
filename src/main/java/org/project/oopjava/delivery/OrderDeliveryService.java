package org.project.oopjava.delivery;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class OrderDeliveryService {
    private final KafkaTemplate<String, OrderDeliveryEvent> kafkaTemplate;
    private final OrderDeliveryStatusRepository statusRepository;
    private final String topic;
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>> subscribers =
            new ConcurrentHashMap<>();

    public OrderDeliveryService(
            KafkaTemplate<String, OrderDeliveryEvent> kafkaTemplate,
            OrderDeliveryStatusRepository statusRepository,
            @Value("${app.kafka.order-delivery-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.statusRepository = statusRepository;
        this.topic = topic;
    }

    public OrderDeliveryEvent publish(
            String orderId,
            OrderDeliveryUpdateRequest request) {
        DeliveryStatus status = parseStatus(request.status());
        OrderDeliveryEvent event = OrderDeliveryEvent.create(
                orderId.trim(), status, request.location(), request.message());
        kafkaTemplate.send(topic, event.orderId(), event);
        return event;
    }

    @KafkaListener(
            topics = "${app.kafka.order-delivery-topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            autoStartup = "${spring.kafka.listener.auto-startup:true}")
    public void consume(OrderDeliveryEvent event) {
        statusRepository.saveLatest(event);
        notifySubscribers(event);
    }

    public Optional<OrderDeliveryEvent> latest(String orderId) {
        return statusRepository.findLatest(orderId);
    }

    public SseEmitter subscribe(String orderId) {
        SseEmitter emitter = new SseEmitter(0L);
        subscribers.computeIfAbsent(orderId, ignored -> new CopyOnWriteArrayList<>()).add(emitter);
        Runnable remove = () -> removeSubscriber(orderId, emitter);
        emitter.onCompletion(remove);
        emitter.onTimeout(remove);
        emitter.onError(ignored -> remove.run());

        latest(orderId).ifPresent(event -> send(emitter, event));
        return emitter;
    }

    private void notifySubscribers(OrderDeliveryEvent event) {
        CopyOnWriteArrayList<SseEmitter> emitters = subscribers.get(event.orderId());
        if (emitters == null) {
            return;
        }
        emitters.removeIf(emitter -> !send(emitter, event));
    }

    private boolean send(SseEmitter emitter, OrderDeliveryEvent event) {
        try {
            emitter.send(SseEmitter.event()
                    .id(event.eventId())
                    .name("delivery-status")
                    .data(event));
            return true;
        } catch (IOException | IllegalStateException exception) {
            emitter.completeWithError(exception);
            return false;
        }
    }

    private void removeSubscriber(String orderId, SseEmitter emitter) {
        CopyOnWriteArrayList<SseEmitter> emitters = subscribers.get(orderId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                subscribers.remove(orderId, emitters);
            }
        }
    }

    private DeliveryStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("status is required");
        }
        try {
            return DeliveryStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unsupported delivery status: " + status, exception);
        }
    }
}
