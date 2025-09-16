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
        }
    }

    public static void dropTables() throws Exception {
        try (var connection = ConnectionManager.getConnection();
             var statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM account");
            statement.executeUpdate("DELETE FROM client");
        }
    }
}
