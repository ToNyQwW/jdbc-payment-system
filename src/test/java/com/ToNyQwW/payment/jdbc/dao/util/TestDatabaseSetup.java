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
        }
    }

    public static void dropTables() throws Exception {
        try (var connection = ConnectionManager.getConnection();
             var statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM account");
            statement.executeUpdate("DELETE FROM client");
            statement.executeUpdate(("DELETE FROM credit_card"));
            statement.executeUpdate("DELETE FROM orders");
        }
    }
}
