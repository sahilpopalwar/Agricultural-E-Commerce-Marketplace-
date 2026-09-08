CREATE TABLE IF NOT EXISTS order_delivery_status (
    order_id VARCHAR(100) PRIMARY KEY,
    event_id VARCHAR(36) NOT NULL,
    status VARCHAR(32) NOT NULL,
    location VARCHAR(255),
    message VARCHAR(1000),
    occurred_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6)
);
