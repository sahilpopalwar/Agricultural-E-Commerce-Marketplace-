package org.project.oopjava.delivery;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

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
}
