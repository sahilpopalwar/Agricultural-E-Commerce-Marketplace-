package org.project.oopjava.order;

import java.math.BigDecimal;
import java.sql.Statement;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderCheckoutService {
    private final JdbcTemplate jdbcTemplate;

    public OrderCheckoutService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public long createOrder(Authentication authentication, CheckoutRequest request) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Authentication is required");
        }
        if (request == null || request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("At least one order item is required");
        }
        if (request.shippingAddress() == null || request.shippingAddress().isBlank()
                || request.shippingAddress().length() > 2000) {
            throw new IllegalArgumentException("A valid shipping address is required");
        }
        if (request.paymentMethod() == null || request.paymentMethod().isBlank()
                || request.paymentMethod().length() > 50) {
            throw new IllegalArgumentException("A valid payment method is required");
        }

        Integer userId = jdbcTemplate.queryForObject(
                "SELECT user_id FROM users WHERE email = ? OR phone = ?",
                Integer.class, authentication.getName(), authentication.getName());
        if (userId == null) {
            throw new IllegalArgumentException("Authenticated user was not found");
        }

        Set<Integer> productIds = new HashSet<>();
        Map<Integer, BigDecimal> prices = new HashMap<>();
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest item : request.items()) {
            if (item == null || item.productId() == null || item.quantity() == null
                    || item.quantity() <= 0 || item.quantity() > 1000
                    || !productIds.add(item.productId())) {
                throw new IllegalArgumentException("Order items are invalid");
            }
            BigDecimal price = jdbcTemplate.queryForObject(
                    "SELECT price FROM products WHERE product_id = ? AND is_active = TRUE",
                    BigDecimal.class, item.productId());
            if (price == null) {
                throw new IllegalArgumentException("Product is unavailable");
            }
            prices.put(item.productId(), price);
            int updated = jdbcTemplate.update("""
                    UPDATE products
                    SET stock_quantity = stock_quantity - ?
                    WHERE product_id = ? AND is_active = TRUE AND stock_quantity >= ?
                    """, item.quantity(), item.productId(), item.quantity());
            if (updated != 1) {
                throw new IllegalArgumentException("Insufficient stock");
            }
            total = total.add(price.multiply(BigDecimal.valueOf(item.quantity())));
        }
        BigDecimal orderTotal = total;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var statement = connection.prepareStatement("""
                    INSERT INTO orders (user_id, total_amount, status, payment_method, shipping_address)
                    VALUES (?, ?, 'PENDING', ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.setBigDecimal(2, orderTotal);
            statement.setString(3, request.paymentMethod());
            statement.setString(4, request.shippingAddress());
            return statement;
        }, keyHolder);
        Number orderId = keyHolder.getKey();
        if (orderId == null) {
            throw new IllegalStateException("Order ID was not generated");
        }

        for (OrderItemRequest item : request.items()) {
            jdbcTemplate.update("""
                    INSERT INTO order_items (order_id, product_id, quantity, price)
                    VALUES (?, ?, ?, ?)
                    """, orderId.longValue(), item.productId(), item.quantity(), prices.get(item.productId()));
        }
        return orderId.longValue();
    }
}
