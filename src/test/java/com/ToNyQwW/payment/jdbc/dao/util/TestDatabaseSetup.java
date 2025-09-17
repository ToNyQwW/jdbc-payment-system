package com.ToNyQwW.payment.jdbc.dao.util;

import com.ToNyQwW.payment.jdbc.util.ConnectionManager;

import java.sql.SQLException;

public class TestDatabaseSetup {

    public static void createTables() throws SQLException {
        try (var connection = ConnectionManager.getConnection();
             var statement = connection.createStatement()) {

            statement.execute("""
                                CREATE TABLE client (
                                    client_id SERIAL PRIMARY KEY,
                                    full_name VARCHAR(100) NOT NULL,
                                    email VARCHAR(100) UNIQUE NOT NULL,
                                    phone VARCHAR(20)
                                )
                    """);

            statement.execute("""
                    CREATE TABLE account
                    (
                        account_id SERIAL PRIMARY KEY,
                        client_id  INT NOT NULL REFERENCES client (client_id) ON DELETE CASCADE,
                        balance    NUMERIC(15, 2) DEFAULT 0.00,
                        is_active  BOOLEAN        DEFAULT TRUE
                    );
                    """);

            statement.execute("""
                    CREATE TYPE order_status AS ENUM ('NEW','PAID','CANCELLED');
                    """);

            statement.execute("""
                    CREATE TABLE credit_card
                    (
                        card_id      SERIAL PRIMARY KEY,
                        account_id   INT            NOT NULL UNIQUE REFERENCES account (account_id) ON DELETE CASCADE,
                        card_number  VARCHAR(16)    NOT NULL UNIQUE,
                        credit_limit NUMERIC(15, 2) NOT NULL,
                        balance      NUMERIC(15, 2) DEFAULT 0.0,
                        is_blocked   BOOLEAN        DEFAULT FALSE
                    );
                    """);

            statement.execute("""
                    CREATE TABLE orders
                    (
                        order_id  SERIAL PRIMARY KEY,
                        client_id INT            NOT NULL REFERENCES client (client_id) ON DELETE CASCADE,
                        amount    NUMERIC(15, 2) NOT NULL,
                        status    order_status DEFAULT 'NEW'
                    );
                    """);

            statement.execute("""
                    CREATE TABLE payment
                    (
                        payment_id   SERIAL PRIMARY KEY,
                        from_account INT            NOT NULL REFERENCES account (account_id),
                        to_account   INT            NOT NULL REFERENCES account (account_id),
                        order_id     INT            NOT NULL REFERENCES orders (order_id),
                        amount       NUMERIC(15, 2) NOT NULL,
                        created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    );
                    """);
        }
    }

    public static void dropTables() throws Exception {
        try (var connection = ConnectionManager.getConnection();
             var statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE payment");
            statement.executeUpdate(("DROP TABLE credit_card"));
            statement.executeUpdate("DROP TABLE account");
            statement.executeUpdate("DROP TABLE orders");
            statement.executeUpdate("DROP TABLE client");
            statement.executeUpdate("DROP TYPE order_status");
        }
    }
}
