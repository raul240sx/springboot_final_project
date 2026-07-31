
CREATE TABLE vendor (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(50) UNIQUE,
    name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    dni VARCHAR(50) NOT NULL,
    password VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,

    PRIMARY KEY (id)
);

CREATE TABLE role (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) UNIQUE NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE vendor_role (
    id BIGINT NOT NULL AUTO_INCREMENT,
    vendor_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_vendor_role_vendor FOREIGN KEY (vendor_id) REFERENCES vendor(id),
    CONSTRAINT fk_vendor_role_role FOREIGN KEY (role_id) REFERENCES role(id),

    INDEX idx_vendor_role_vendor_id (vendor_id),
    INDEX idx_vendor_role_role_id (role_id)
);

CREATE TABLE refresh_token (
    id BIGINT NOT NULL AUTO_INCREMENT,
    token VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    vendor_id BIGINT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_refresh_token_vendor FOREIGN KEY (vendor_id) REFERENCES vendor(id),

    INDEX idx_refresh_token_value (token)
);