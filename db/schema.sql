CREATE TABLE client
(
    client_id SERIAL PRIMARY KEY,
    full_name VARCHAR(100)        NOT NULL,
    email     VARCHAR(100) UNIQUE NOT NULL,
    phone     VARCHAR(20)
);

CREATE TABLE account
(
    account_id SERIAL PRIMARY KEY,
    client_id  INT NOT NULL REFERENCES client (client_id) ON DELETE CASCADE,
    balance    NUMERIC(15, 2) DEFAULT 0.00,
    is_active  BOOLEAN        DEFAULT TRUE
);

CREATE TABLE credit_card
(
    card_id      SERIAL PRIMARY KEY,
    account_id   INT            NOT NULL UNIQUE REFERENCES account (account_id) ON DELETE CASCADE,
    card_number  VARCHAR(16)    NOT NULL UNIQUE,
    credit_limit NUMERIC(15, 2) NOT NULL,
    balance      NUMERIC(15, 2) DEFAULT 0.0,
    is_blocked   BOOLEAN        DEFAULT FALSE
);

CREATE TYPE order_status AS ENUM ('NEW','PAID','CANCELLED');

CREATE TABLE orders
(
    order_id  SERIAL PRIMARY KEY,
    client_id INT            NOT NULL REFERENCES client (client_id) ON DELETE CASCADE,
    amount    NUMERIC(15, 2) NOT NULL,
    status    order_status DEFAULT 'NEW'
);

CREATE TABLE payment
(
    payment_id   SERIAL PRIMARY KEY,
    from_account INT            NOT NULL REFERENCES account (account_id),
    to_account   INT            NOT NULL REFERENCES account (account_id),
    order_id     INT            NOT NULL REFERENCES orders (order_id),
    amount       NUMERIC(15, 2) NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);