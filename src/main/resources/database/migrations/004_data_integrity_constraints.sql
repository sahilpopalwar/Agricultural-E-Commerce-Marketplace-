-- Apply once to an existing MySQL database after checking for duplicates.
ALTER TABLE wishlist
    ADD CONSTRAINT uq_wishlist_user_product UNIQUE (user_id, product_id);

ALTER TABLE shopping_cart
    ADD CONSTRAINT uq_cart_user_product UNIQUE (user_id, product_id);

ALTER TABLE products
    ADD CONSTRAINT chk_product_price CHECK (price >= 0),
    ADD CONSTRAINT chk_product_stock CHECK (stock_quantity >= 0);

ALTER TABLE order_items
    ADD CONSTRAINT chk_order_item_quantity CHECK (quantity > 0),
    ADD CONSTRAINT chk_order_item_price CHECK (price >= 0);
