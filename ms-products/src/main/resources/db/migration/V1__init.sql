CREATE TABLE product (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(50) UNIQUE,
    name VARCHAR(100) NOT NULL,
    brand VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    stock INT NOT NULL DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,

    PRIMARY KEY (id),
    CONSTRAINT chk_price_positive CHECK (price >= 0),
    CONSTRAINT chk_stock_positive CHECK (stock >= 0)
);