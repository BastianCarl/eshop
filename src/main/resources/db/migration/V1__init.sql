

-- ==========================================================
-- PRODUCT
-- ==========================================================
CREATE TABLE products (
    id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name   VARCHAR(255) NOT NULL,
    price  DECIMAL(19, 2) NOT NULL,
    stock  INT NOT NULL DEFAULT 0
);