-- Flyway migration: initial e-commerce schema
-- Tables: users, promotions, products, orders, order_items, wishlists, wishlist_items

-- gen_random_uuid() is built into PostgreSQL core since v13, so no
-- extension (pgcrypto/uuid-ossp) is required — and your DB user doesn't
-- need CREATE EXTENSION privileges for this script to run.

-- ==========================================================
-- USER
-- ==========================================================
CREATE TABLE users (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email      VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    CONSTRAINT uk_users_email UNIQUE (email)
);

-- ==========================================================
-- PROMOTION
-- ==========================================================
CREATE TABLE promotions (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code           VARCHAR(50) NOT NULL,
    discount_type  VARCHAR(50) NOT NULL,
    value          DECIMAL(19, 2) NOT NULL,
    valid_until    TIMESTAMP,
    CONSTRAINT uk_promotions_code UNIQUE (code)
);

-- ==========================================================
-- PRODUCT
-- ==========================================================
CREATE TABLE products (
    id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name   VARCHAR(255) NOT NULL,
    price  DECIMAL(19, 2) NOT NULL,
    stock  INT NOT NULL DEFAULT 0
);

-- ==========================================================
-- ORDER (depends on users, promotions)
-- ==========================================================
CREATE TABLE orders (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id        UUID NOT NULL,
    promotion_id   UUID,
    status         VARCHAR(50) NOT NULL,
    total          DECIMAL(19, 2) NOT NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_orders_promotion
        FOREIGN KEY (promotion_id) REFERENCES promotions (id)
);

CREATE INDEX idx_orders_user_id ON orders (user_id);
CREATE INDEX idx_orders_promotion_id ON orders (promotion_id);

-- ==========================================================
-- ORDER_ITEM (depends on orders, products)
-- ==========================================================
CREATE TABLE order_items (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id    UUID NOT NULL,
    product_id  UUID NOT NULL,
    quantity    INT NOT NULL,
    unit_price  DECIMAL(19, 2) NOT NULL,
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product
        FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE INDEX idx_order_items_order_id ON order_items (order_id);
CREATE INDEX idx_order_items_product_id ON order_items (product_id);

-- ==========================================================
-- WISHLIST (depends on users; one user has at most one wishlist)
-- ==========================================================
CREATE TABLE wishlists (
    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id  UUID NOT NULL,
    name     VARCHAR(255) NOT NULL,
    CONSTRAINT fk_wishlists_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uk_wishlists_user_id UNIQUE (user_id)
);

CREATE INDEX idx_wishlists_user_id ON wishlists (user_id);

-- ==========================================================
-- WISHLIST_ITEM (depends on wishlists, products)
-- ==========================================================
CREATE TABLE wishlist_items (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    wishlist_id  UUID NOT NULL,
    product_id   UUID NOT NULL,
    added_at     TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT fk_wishlist_items_wishlist
        FOREIGN KEY (wishlist_id) REFERENCES wishlists (id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_items_product
        FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT uk_wishlist_items_wishlist_product UNIQUE (wishlist_id, product_id)
);

CREATE INDEX idx_wishlist_items_wishlist_id ON wishlist_items (wishlist_id);
CREATE INDEX idx_wishlist_items_product_id ON wishlist_items (product_id);