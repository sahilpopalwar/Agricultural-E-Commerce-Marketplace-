-- Create users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    farm_name VARCHAR(150),
    farm_address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS otp_verifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) NOT NULL,
    otp_hash CHAR(64) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP NULL,
    attempts INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_otp_phone_created (phone, created_at)
);

CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    token_hash CHAR(64) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_password_reset_expiry (expires_at)
);

-- Create user_settings table
CREATE TABLE IF NOT EXISTS user_settings (
    user_id INT PRIMARY KEY,
    language VARCHAR(10) DEFAULT 'en',
    email_notifications BOOLEAN DEFAULT true,
    push_notifications BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create translations table
CREATE TABLE IF NOT EXISTS translations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    language VARCHAR(10) NOT NULL,
    `key` VARCHAR(100) NOT NULL,
    value TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_translation (language, `key`)
);

-- Latest materialized order delivery status. The full event history remains in Kafka.
CREATE TABLE IF NOT EXISTS order_delivery_status (
    order_id INT PRIMARY KEY,
    event_id VARCHAR(36) NOT NULL,
    status VARCHAR(32) NOT NULL,
    location VARCHAR(255),
    message VARCHAR(1000),
    occurred_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE IF NOT EXISTS categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    image VARCHAR(255) NOT NULL,
    category_id INT NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

CREATE TABLE IF NOT EXISTS orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL CHECK (total_amount >= 0),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payment_method VARCHAR(50) NOT NULL,
    shipping_address TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    INDEX idx_orders_user_status (user_id, status)
);

ALTER TABLE order_delivery_status
    ADD CONSTRAINT fk_delivery_status_order
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE IF NOT EXISTS wishlist (
    wishlist_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_wishlist_user_product (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE IF NOT EXISTS shopping_cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1 CHECK (quantity > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_cart_user_product (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Insert default translations
INSERT INTO translations (language, `key`, value) VALUES
-- English translations
('en', 'welcome', 'Welcome to Fresh Market'),
('en', 'products', 'Products'),
('en', 'categories', 'Categories'),
('en', 'cart', 'Shopping Cart'),
('en', 'wishlist', 'Wishlist'),
('en', 'orders', 'Orders'),
('en', 'profile', 'Profile'),
('en', 'settings', 'Settings'),
('en', 'logout', 'Logout'),
('en', 'login', 'Login'),
('en', 'register', 'Register'),
('en', 'search', 'Search products...'),
('en', 'add_to_cart', 'Add to Cart'),
('en', 'view_details', 'View Details'),
('en', 'price', 'Price'),
('en', 'quantity', 'Quantity'),
('en', 'total', 'Total'),
('en', 'checkout', 'Checkout'),
('en', 'continue_shopping', 'Continue Shopping'),
('en', 'empty_cart', 'Your cart is empty'),
('en', 'empty_wishlist', 'Your wishlist is empty'),
('en', 'no_orders', 'No orders found'),
('en', 'order_status', 'Order Status'),
('en', 'delivery_date', 'Expected Delivery'),
('en', 'track_order', 'Track Order'),
('en', 'contact_us', 'Contact Us'),
('en', 'about_us', 'About Us'),
('en', 'terms', 'Terms & Conditions'),
('en', 'privacy', 'Privacy Policy'),
('en', 'copyright', '© 2023 Fresh Market. All rights reserved.'),
('en', 'current_settings', 'Current Settings'),
('en', 'select_language', 'Select Language'),
('en', 'save_changes', 'Save Changes'),
('en', 'settings_saved', 'Settings saved successfully!'),
('en', 'save_error', 'Failed to save settings'),
('en', 'current_language', 'Current Language'),
('en', 'loading', 'Loading...'),

-- Marathi translations
('mr', 'welcome', 'फ्रेश मार्केटमध्ये आपले स्वागत आहे'),
('mr', 'products', 'उत्पादने'),
('mr', 'categories', 'श्रेण्या'),
('mr', 'cart', 'शॉपिंग कार्ट'),
('mr', 'wishlist', 'विशलिस्ट'),
('mr', 'orders', 'ऑर्डर्स'),
('mr', 'profile', 'प्रोफाइल'),
('mr', 'settings', 'सेटिंग्ज'),
('mr', 'logout', 'लॉगआउट'),
('mr', 'login', 'लॉगिन'),
('mr', 'register', 'नोंदणी करा'),
('mr', 'search', 'उत्पादने शोधा...'),
('mr', 'add_to_cart', 'कार्टमध्ये जोडा'),
('mr', 'view_details', 'तपशील पहा'),
('mr', 'price', 'किंमत'),
('mr', 'quantity', 'प्रमाण'),
('mr', 'total', 'एकूण'),
('mr', 'checkout', 'चेकआउट'),
('mr', 'continue_shopping', 'खरेदी सुरू ठेवा'),
('mr', 'empty_cart', 'तुमची कार्ट रिकामी आहे'),
('mr', 'empty_wishlist', 'तुमची विशलिस्ट रिकामी आहे'),
('mr', 'no_orders', 'कोणतेही ऑर्डर सापडले नाहीत'),
('mr', 'order_status', 'ऑर्डर स्थिती'),
('mr', 'delivery_date', 'अपेक्षित वितरण तारीख'),
('mr', 'track_order', 'ऑर्डर ट्रॅक करा'),
('mr', 'contact_us', 'आमच्याशी संपर्क साधा'),
('mr', 'about_us', 'आमच्याबद्दल'),
('mr', 'terms', 'अटी आणि नियम'),
('mr', 'privacy', 'गोपनीयता धोरण'),
('mr', 'copyright', '© 2023 फ्रेश मार्केट. सर्व हक्क राखीव.'),
('mr', 'current_settings', 'वर्तमान सेटिंग्ज'),
('mr', 'select_language', 'भाषा निवडा'),
('mr', 'save_changes', 'बदल जतन करा'),
('mr', 'settings_saved', 'सेटिंग्ज यशस्वीरित्या जतन केले!'),
('mr', 'save_error', 'सेटिंग्ज जतन करण्यात अयशस्वी'),
('mr', 'current_language', 'वर्तमान भाषा'),
('mr', 'loading', 'लोड करत आहे...'),

-- Hindi translations
('hi', 'welcome', 'फ्रेश मार्केट में आपका स्वागत है'),
('hi', 'products', 'उत्पाद'),
('hi', 'categories', 'श्रेणियाँ'),
('hi', 'cart', 'शॉपिंग कार्ट'),
('hi', 'wishlist', 'विशलिस्ट'),
('hi', 'orders', 'ऑर्डर'),
('hi', 'profile', 'प्रोफ़ाइल'),
('hi', 'settings', 'सेटिंग्स'),
('hi', 'logout', 'लॉगआउट'),
('hi', 'login', 'लॉगिन'),
('hi', 'register', 'रजिस्टर करें'),
('hi', 'search', 'उत्पाद खोजें...'),
('hi', 'add_to_cart', 'कार्ट में जोड़ें'),
('hi', 'view_details', 'विवरण देखें'),
('hi', 'price', 'कीमत'),
('hi', 'quantity', 'मात्रा'),
('hi', 'total', 'कुल'),
('hi', 'checkout', 'चेकआउट'),
('hi', 'continue_shopping', 'खरीदारी जारी रखें'),
('hi', 'empty_cart', 'आपकी कार्ट खाली है'),
('hi', 'empty_wishlist', 'आपकी विशलिस्ट खाली है'),
('hi', 'no_orders', 'कोई ऑर्डर नहीं मिला'),
('hi', 'order_status', 'ऑर्डर स्थिति'),
('hi', 'delivery_date', 'संभावित डिलीवरी'),
('hi', 'track_order', 'ऑर्डर ट्रैक करें'),
('hi', 'contact_us', 'संपर्क करें'),
('hi', 'about_us', 'हमारे बारे में'),
('hi', 'terms', 'नियम और शर्तें'),
('hi', 'privacy', 'गोपनीयता नीति'),
('hi', 'copyright', '© 2023 फ्रेश मार्केट. सर्वाधिकार सुरक्षित।'),
('hi', 'current_settings', 'वर्तमान सेटिंग्स'),
('hi', 'select_language', 'भाषा चुनें'),
('hi', 'save_changes', 'परिवर्तन सहेजें'),
('hi', 'settings_saved', 'सेटिंग्स सफलतापूर्वक सहेजी गईं!'),
('hi', 'save_error', 'सेटिंग्स सहेजने में विफल'),
('hi', 'current_language', 'वर्तमान भाषा'),
('hi', 'loading', 'लोड हो रहा है...'); 