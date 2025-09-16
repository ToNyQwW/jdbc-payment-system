package com.ToNyQwW.payment.jdbc.dao;

import com.ToNyQwW.payment.jdbc.entity.Client;
import com.ToNyQwW.payment.jdbc.util.ConnectionManager;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientDaoTest {

    private static final ClientDao clientDao = ClientDao.getInstance();

    @BeforeAll
    void setupSchema() throws Exception {
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
        }
    }

    @BeforeEach
    void cleanTable() throws Exception {
        try (var connection = ConnectionManager.getConnection();
             var statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE client");
        }
    }

    @AfterAll
    static void afterAll() {
        ConnectionManager.closeConnectionPool();
    }

    @Test
    void save() {
        Client client = new Client("Alice", "alice@example.com", "123456");
        clientDao.save(client);

        assertTrue(client.getClientId() > 0);
    }

    @Test
    void findById() {
        Client client = clientDao.save(new Client("Bob", "bob@example.com", "111"));
        var found = clientDao.findById(client.getClientId());

        assertTrue(found.isPresent());
        assertEquals("Bob", found.get().getFullName());
    }

    @Test
    void findAll() {
        clientDao.save(new Client("Charlie", "charlie@example.com", "222"));
        clientDao.save(new Client("Diana", "diana@example.com", "333"));

        List<Client> clients = clientDao.findAll();
        assertEquals(2, clients.size());
    }

    @Test
    void update() {
        Client client = clientDao.save(new Client("Eve", "eve@example.com", "444"));
        client.setFullName("Eva");
        clientDao.update(client);

        var updated = clientDao.findById(client.getClientId()).orElseThrow();
        assertEquals("Eva", updated.getFullName());
    }

    @Test
    void delete() {
        Client client = clientDao.save(new Client("Frank", "frank@example.com", "555"));
        boolean deleted = clientDao.delete(client.getClientId());

        assertTrue(deleted);
        assertTrue(clientDao.findById(client.getClientId()).isEmpty());
    }
}