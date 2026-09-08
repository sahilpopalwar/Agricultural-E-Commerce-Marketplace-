-- Apply after verifying order_delivery_status contains only numeric order IDs.
ALTER TABLE order_delivery_status
    MODIFY order_id INT NOT NULL;

ALTER TABLE order_delivery_status
    ADD CONSTRAINT fk_delivery_status_order
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE;
