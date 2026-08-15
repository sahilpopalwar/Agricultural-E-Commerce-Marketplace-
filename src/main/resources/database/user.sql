-- Fresh Market Database Schema
-- Complete setup in one file

-- Create the database
CREATE DATABASE IF NOT EXISTS fresh_market;
USE fresh_market;

-- Users Table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    password_hash VARCHAR(255) NOT NULL,
    profile_image VARCHAR(255),
    address TEXT,
    city VARCHAR(50),
    state VARCHAR(50),
    zip_code VARCHAR(20),
    country VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Categories Table
CREATE TABLE categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products Table
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    image VARCHAR(255) NOT NULL,
    category_id INT NOT NULL,
    stock_quantity INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

-- Orders Table
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    order_number VARCHAR(20) UNIQUE NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending',
    payment_method VARCHAR(50),
    payment_status ENUM('pending', 'completed', 'failed', 'refunded') DEFAULT 'pending',
    shipping_address TEXT NOT NULL,
    billing_address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Order Items Table
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) GENERATED ALWAYS AS (quantity * unit_price) STORED,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Wishlist Table
CREATE TABLE wishlist (
    wishlist_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- User Activity Table
CREATE TABLE user_activity (
    activity_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    activity_type ENUM('view', 'purchase', 'search', 'wishlist') NOT NULL,
    product_id INT,
    search_query VARCHAR(255),
    activity_count INT DEFAULT 1,
    activity_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE SET NULL
);

-- Delivery Tracking Table
CREATE TABLE delivery_tracking (
    tracking_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    status ENUM('order_placed', 'processing', 'shipped', 'on_the_way', 'delivered') NOT NULL,
    estimated_delivery TIMESTAMP,
    current_location VARCHAR(255),
    notes TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- User Settings Table
CREATE TABLE user_settings (
    setting_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    show_profile BOOLEAN DEFAULT TRUE,
    personalized_recommendations BOOLEAN DEFAULT TRUE,
    show_purchase_history BOOLEAN DEFAULT FALSE,
    email_notifications BOOLEAN DEFAULT TRUE,
    sms_notifications BOOLEAN DEFAULT FALSE,
    push_notifications BOOLEAN DEFAULT TRUE,
    two_factor_auth BOOLEAN DEFAULT FALSE,
    login_alerts BOOLEAN DEFAULT TRUE,
    language VARCHAR(10) DEFAULT 'en',
    timezone VARCHAR(50) DEFAULT 'UTC',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Shopping Cart Table
CREATE TABLE shopping_cart (
    cart_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    UNIQUE KEY (user_id, product_id)
);

-- Create indexes for performance
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_product_category ON products(category_id);
CREATE INDEX idx_order_user ON orders(user_id);
CREATE INDEX idx_activity_user ON user_activity(user_id);
CREATE INDEX idx_activity_date ON user_activity(activity_date);
CREATE INDEX idx_wishlist_user ON wishlist(user_id);

-- Add full-text search for products
ALTER TABLE products ADD FULLTEXT(name, description);

-- Sample Data Insertion

-- Insert categories
INSERT INTO categories (name, description) VALUES 
('Fruits', 'Fresh seasonal fruits'),
('Vegetables', 'Organic vegetables'),
('Dairy', 'Milk, cheese, and other dairy products'),
('Meat', 'Fresh meat products'),
('Bakery', 'Freshly baked goods');

-- Insert products
INSERT INTO products (name, description, price, image, category_id, stock_quantity) VALUES
('Apple', 'Fresh and juicy apples from local farms', 2.00, 'apple.jpg', 1, 100),
('Carrot', 'Organic carrots packed with nutrients', 1.00, 'carrot.jpg', 2, 150),
('Banana', 'Sweet and nutritious bananas', 0.50, 'banana.jpg', 1, 200),
('Spinach', 'Fresh organic spinach leaves', 1.20, 'spinach.jpg', 2, 80),
('Bread', 'Whole wheat freshly baked bread', 2.50, 'bread.jpg', 5, 50),
('Milk', 'Fresh whole milk', 3.00, 'milk.jpg', 3, 120),
('Cheese', 'Aged cheddar cheese', 4.50, 'cheese.jpg', 3, 60),
('Chicken Breast', 'Boneless skinless chicken breast', 7.99, 'chicken.jpg', 4, 40),
('Croissant', 'Buttery French croissant', 1.75, 'croissant.jpg', 5, 75);

-- Insert a sample user
INSERT INTO users (first_name, last_name, email, phone, password_hash, profile_image, address, city, state, zip_code, country) 
VALUES ('Sarah', 'Johnson', 'sarah@example.com', '+1234567890', '$2a$10$xJwL5v5Jz5UZJZJZJZJZJeJwL5v5Jz5UZJZJZJZJZJe', 'profile.jpg', '123 Main St', 'Nanded', 'Maharashtra', '431605', 'India');

-- Insert user settings
INSERT INTO user_settings (user_id) VALUES (1);

-- Insert sample wishlist items
INSERT INTO wishlist (user_id, product_id) VALUES 
(1, 1),
(1, 4),
(1, 5);

-- Insert sample shopping cart items
INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES 
(1, 2, 3),
(1, 3, 2);

-- Insert sample activity
INSERT INTO user_activity (user_id, activity_type, product_id, activity_count, activity_date) VALUES
(1, 'view', 1, 3, '2023-06-15 10:00:00'),
(1, 'purchase', 1, 2, '2023-06-10 14:30:00'),
(1, 'view', 2, 5, '2023-06-14 11:20:00'),
(1, 'purchase', 2, 1, '2023-06-05 09:15:00'),
(1, 'view', 3, 2, '2023-06-12 16:45:00'),
(1, 'wishlist', 4, 1, '2023-06-01 13:10:00');

-- Insert sample order
INSERT INTO orders (user_id, order_number, total_amount, status, payment_method, payment_status, shipping_address) 
VALUES (1, 'FM-2023-0567', 24.97, 'shipped', 'Credit Card', 'completed', '123 Main St, Nanded, Maharashtra 431605');

-- Insert order items
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(1, 1, 2, 2.00),
(1, 2, 3, 1.00),
(1, 5, 1, 2.50);

-- Insert delivery tracking
INSERT INTO delivery_tracking (order_id, status, estimated_delivery, current_location) 
VALUES (1, 'shipped', '2023-06-17 17:00:00', 'Mumbai Distribution Center');

-- Create a view for product catalog
CREATE VIEW product_catalog AS
SELECT p.product_id, p.name, p.description, p.price, p.image, p.stock_quantity, 
       c.name AS category_name, c.category_id
FROM products p
JOIN categories c ON p.category_id = c.category_id
WHERE p.is_active = TRUE;

-- Create a view for user wishlists
CREATE VIEW user_wishlists AS
SELECT u.user_id, CONCAT(u.first_name, ' ', u.last_name) AS user_name, 
       w.wishlist_id, w.created_at AS wishlist_date,
       p.product_id, p.name AS product_name, p.price, p.image
FROM wishlist w
JOIN users u ON w.user_id = u.user_id
JOIN products p ON w.product_id = p.product_id;

-- Create a stored procedure for placing an order
DELIMITER //
CREATE PROCEDURE place_order(
    IN p_user_id INT,
    IN p_payment_method VARCHAR(50),
    IN p_shipping_address TEXT
)
BEGIN
    DECLARE v_order_number VARCHAR(20);
    DECLARE v_total DECIMAL(10,2);
    DECLARE v_order_id INT;
    
    -- Generate order number
    SET v_order_number = CONCAT('FM-', YEAR(CURDATE()), '-', LPAD(FLOOR(RAND() * 10000), 4, 0));
    
    -- Calculate cart total
    SELECT SUM(sc.quantity * p.price) INTO v_total
    FROM shopping_cart sc
    JOIN products p ON sc.product_id = p.product_id
    WHERE sc.user_id = p_user_id;
    
    -- Create order
    INSERT INTO orders (user_id, order_number, total_amount, payment_method, shipping_address)
    VALUES (p_user_id, v_order_number, v_total, p_payment_method, p_shipping_address);
    
    SET v_order_id = LAST_INSERT_ID();
    
    -- Add order items
    INSERT INTO order_items (order_id, product_id, quantity, unit_price)
    SELECT v_order_id, sc.product_id, sc.quantity, p.price
    FROM shopping_cart sc
    JOIN products p ON sc.product_id = p.product_id
    WHERE sc.user_id = p_user_id;
    
    -- Clear shopping cart
    DELETE FROM shopping_cart WHERE user_id = p_user_id;
    
    -- Create delivery tracking
    INSERT INTO delivery_tracking (order_id, status)
    VALUES (v_order_id, 'order_placed');
    
    -- Return order details
    SELECT * FROM orders WHERE order_id = v_order_id;
END //
DELIMITER ;

-- Create trigger to update product stock after order
DELIMITER //
CREATE TRIGGER after_order_item_insert
AFTER INSERT ON order_items
FOR EACH ROW
BEGIN
    UPDATE products 
    SET stock_quantity = stock_quantity - NEW.quantity
    WHERE product_id = NEW.product_id;
END //
DELIMITER ;