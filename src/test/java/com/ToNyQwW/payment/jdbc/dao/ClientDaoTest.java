package com.ToNyQwW.payment.jdbc.dao;

import com.ToNyQwW.payment.jdbc.dao.util.TestDatabaseSetup;
import com.ToNyQwW.payment.jdbc.entity.Client;
import com.ToNyQwW.payment.jdbc.util.ConnectionManager;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientDaoTest {

    private static final ClientDao clientDao = ClientDao.getInstance();

    @BeforeEach
    void beforeEach() throws Exception {
        TestDatabaseSetup.createTables();
    }

    @AfterEach
    void afterEach() throws Exception {
        TestDatabaseSetup.dropTables();
    }

    @AfterAll
    static void afterAll() {
        ConnectionManager.closeConnectionPool();
    }

    @Test
    void save() {
        Client client = new Client("Alice", "alice@example.com", "123456");
        clientDao.save(client);

        assertTrue(client.getClientId() > 0, "account_id должен быть сгенерирован");
    }

    @Test
    void findById() {
        Client client = clientDao.save(new Client("Bob", "bob@example.com", "111"));
        var found = clientDao.findById(client.getClientId());

        assertTrue(found.isPresent(), "Аккаунт должен находиться по id");
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