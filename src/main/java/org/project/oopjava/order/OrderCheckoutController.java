package org.project.oopjava.order;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderCheckoutController {
    private final OrderCheckoutService checkoutService;

    public OrderCheckoutController(OrderCheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(
            Authentication authentication,
            @RequestBody CheckoutRequest request) {
        long orderId = checkoutService.createOrder(authentication, request);
        return ResponseEntity.ok(Map.of("orderId", orderId, "status", "PENDING"));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest() {
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid order request"));
    }
}
