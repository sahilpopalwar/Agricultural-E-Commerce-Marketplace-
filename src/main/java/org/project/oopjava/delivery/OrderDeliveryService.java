package org.project.oopjava.delivery;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class OrderDeliveryService {
    private static final Logger log = LoggerFactory.getLogger(OrderDeliveryService.class);
    private static final long SSE_TIMEOUT_MS = 30 * 60 * 1000L;
    private static final int MAX_SUBSCRIBERS_PER_ORDER = 100;
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
        if (orderId == null || !orderId.trim().matches("\\d{1,10}")) {
            throw new IllegalArgumentException("Invalid order id");
        }
        if (request == null) {
            throw new IllegalArgumentException("Delivery request is required");
        }
        if (request.location() != null && request.location().length() > 255
                || request.message() != null && request.message().length() > 1000) {
            throw new IllegalArgumentException("Delivery fields are too long");
        }
        DeliveryStatus status = parseStatus(request.status());
        OrderDeliveryEvent event = OrderDeliveryEvent.create(
                orderId.trim(), status, request.location(), request.message());
        try {
            CompletableFuture<?> result = kafkaTemplate.send(topic, event.orderId(), event);
            if (result != null) {
                result.get(5, TimeUnit.SECONDS);
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            log.error("Kafka delivery publish interrupted for order {}", event.orderId(), exception);
            throw new DeliveryPublishException();
        } catch (ExecutionException | TimeoutException | RuntimeException exception) {
            log.error("Kafka delivery publish failed for order {}", event.orderId(), exception);
            throw new DeliveryPublishException();
        }
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

    public boolean canAccess(String orderId, Authentication authentication) {
        return statusRepository.canAccess(orderId, authentication);
    }

    public boolean canManage(String orderId, Authentication authentication) {
        return statusRepository.canManage(orderId, authentication);
    }

    public SseEmitter subscribe(String orderId) {
        if (orderId == null || !orderId.matches("\\d{1,10}")) {
            throw new IllegalArgumentException("Invalid order id");
        }
        CopyOnWriteArrayList<SseEmitter> orderSubscribers =
                subscribers.computeIfAbsent(orderId, ignored -> new CopyOnWriteArrayList<>());
        if (orderSubscribers.size() >= MAX_SUBSCRIBERS_PER_ORDER) {
            throw new IllegalStateException("Too many subscribers");
        }
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT_MS);
        orderSubscribers.add(emitter);
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
