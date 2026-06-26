CREATE TABLE client (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(20) UNIQUE,
    name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    dni VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,

    PRIMARY KEY (id)
);


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


CREATE TABLE sale (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(50) UNIQUE,
    date DATE NOT NULL,
    total_amount DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    client_id BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,

    PRIMARY KEY (id),
    CONSTRAINT fk_sale_client FOREIGN KEY (client_id) REFERENCES client(id),
    CONSTRAINT chk_total_amount_positive CHECK (total_amount >= 0),

    INDEX idx_sale_client_id (client_id)
);


CREATE TABLE detail (
    id BIGINT NOT NULL AUTO_INCREMENT,
    sale_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    partial_amount DECIMAL(19, 2) NOT NULL DEFAULT 0.00,

    PRIMARY KEY (id),
    CONSTRAINT fk_detail_sale FOREIGN KEY (sale_id) REFERENCES sale(id),
    CONSTRAINT fk_detail_product FOREIGN KEY (product_id) REFERENCES product(id),
    CONSTRAINT chk_partial_amount_positive CHECK (partial_amount >= 0),
    CONSTRAINT chk_quantity_positive CHECK (quantity > 0),

    INDEX idx_detail_sale_id (sale_id),
    INDEX idx_detail_product_id (product_id)
);