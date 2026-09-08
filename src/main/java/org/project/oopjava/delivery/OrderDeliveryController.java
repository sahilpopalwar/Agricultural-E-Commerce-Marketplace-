package org.project.oopjava.delivery;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/orders")
public class OrderDeliveryController {
    private final OrderDeliveryService deliveryService;

    public OrderDeliveryController(OrderDeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/{orderId}/delivery")
    @PreAuthorize("hasAnyRole('ADMIN', 'FARMER')")
    public ResponseEntity<OrderDeliveryEvent> publishDeliveryUpdate(
            @PathVariable String orderId,
            @RequestBody OrderDeliveryUpdateRequest request) {
        return ResponseEntity.accepted().body(deliveryService.publish(orderId, request));
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'FARMER')")
    public ResponseEntity<OrderDeliveryEvent> updateDeliveryStatus(
            @PathVariable String orderId,
            @RequestBody OrderDeliveryUpdateRequest request) {
        return publishDeliveryUpdate(orderId, request);
    }

    @PostMapping("/delivery")
    @PreAuthorize("hasAnyRole('ADMIN', 'FARMER')")
    public ResponseEntity<OrderDeliveryEvent> publishDeliveryUpdate(
            @RequestBody Map<String, String> request) {
        String orderId = request.get("orderId");
        if (orderId == null || orderId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return publishDeliveryUpdate(
                orderId,
                new OrderDeliveryUpdateRequest(
                        request.get("status"), request.get("location"), request.get("message")));
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<OrderDeliveryEvent> getDeliveryStatus(
            @PathVariable String orderId, Authentication authentication) {
        if (!deliveryService.canAccess(orderId, authentication)) {
            return ResponseEntity.notFound().build();
        }
        return deliveryService.latest(orderId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{orderId}/delivery")
    public ResponseEntity<OrderDeliveryEvent> getDelivery(
            @PathVariable String orderId, Authentication authentication) {
        return getDeliveryStatus(orderId, authentication);
    }

    @GetMapping(value = "/{orderId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamDeliveryStatus(
            @PathVariable String orderId, Authentication authentication) {
        if (!deliveryService.canAccess(orderId, authentication)) {
            throw new IllegalArgumentException("Order is not accessible");
        }
        return deliveryService.subscribe(orderId);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid delivery request"));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DeliveryPublishException.class)
    public ResponseEntity<Map<String, String>> publishFailure() {
        return ResponseEntity.status(503).body(Map.of("error", "Delivery update is temporarily unavailable"));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> serverError() {
        return ResponseEntity.internalServerError().body(Map.of("error", "Request could not be completed"));
    }
}
