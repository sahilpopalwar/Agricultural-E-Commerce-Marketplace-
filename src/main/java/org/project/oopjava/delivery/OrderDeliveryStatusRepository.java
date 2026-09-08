package org.project.oopjava.delivery;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import org.springframework.security.core.Authentication;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class OrderDeliveryStatusRepository {
    private static final String UPDATE_SQL = """
        UPDATE order_delivery_status
        SET event_id = ?, status = ?, location = ?, message = ?,
            occurred_at = ?, updated_at = CURRENT_TIMESTAMP
        WHERE order_id = ? AND occurred_at <= ?
        """;

    private static final String INSERT_SQL = """
        INSERT INTO order_delivery_status
            (order_id, event_id, status, location, message, occurred_at, updated_at)
        VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
        """;

    private static final String FIND_SQL = """
        SELECT order_id, event_id, status, location, message, occurred_at
        FROM order_delivery_status
        WHERE order_id = ?
        """;

    private final JdbcTemplate jdbcTemplate;

    public OrderDeliveryStatusRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void saveLatest(OrderDeliveryEvent event) {
        Timestamp occurredAt = Timestamp.from(event.occurredAt());
        int updated = jdbcTemplate.update(
                UPDATE_SQL,
                event.eventId(),
                event.status().name(),
                event.location(),
                event.message(),
                occurredAt,
                event.orderId(),
                occurredAt);
        if (updated > 0) {
            return;
        }

        try {
            jdbcTemplate.update(
                    INSERT_SQL,
                    event.orderId(),
                    event.eventId(),
                    event.status().name(),
                    event.location(),
                    event.message(),
                    occurredAt);
        } catch (DuplicateKeyException duplicate) {
            // Another consumer inserted the order between UPDATE and INSERT.
            jdbcTemplate.update(
                    UPDATE_SQL,
                    event.eventId(),
                    event.status().name(),
                    event.location(),
                    event.message(),
                    occurredAt,
                    event.orderId(),
                    occurredAt);
        }
    }

    public Optional<OrderDeliveryEvent> findLatest(String orderId) {
        return jdbcTemplate.query(FIND_SQL, rs -> {
            if (!rs.next()) {
                return Optional.empty();
            }
            return Optional.of(new OrderDeliveryEvent(
                    rs.getString("event_id"),
                    rs.getString("order_id"),
                    DeliveryStatus.valueOf(rs.getString("status")),
                    rs.getString("location"),
                    rs.getString("message"),
                    rs.getTimestamp("occurred_at").toInstant()));
        }, orderId);
    }

    public boolean canAccess(String orderId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        if (hasRole(authentication, "ADMIN")) {
            return true;
        }
        return isOrderOwner(orderId, authentication);
    }

    public boolean canManage(String orderId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return hasRole(authentication, "ADMIN")
                || hasRole(authentication, "FARMER") && isOrderOwner(orderId, authentication);
    }

    private boolean isOrderOwner(String orderId, Authentication authentication) {
        Integer owner = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM orders o
                JOIN users u ON u.user_id = o.user_id
                WHERE CAST(o.order_id AS CHAR) = ?
                  AND (u.email = ? OR u.phone = ?)
                """, Integer.class, orderId, authentication.getName(), authentication.getName());
        return owner != null && owner > 0;
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }
}
