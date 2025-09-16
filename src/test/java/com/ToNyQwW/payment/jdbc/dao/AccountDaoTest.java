package com.ToNyQwW.payment.jdbc.dao;

import com.ToNyQwW.payment.jdbc.dao.util.TestDatabaseSetup;
import com.ToNyQwW.payment.jdbc.entity.Account;
import com.ToNyQwW.payment.jdbc.entity.Client;
import com.ToNyQwW.payment.jdbc.util.ConnectionManager;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountDaoTest {

    private static final AccountDao accountDao = AccountDao.getInstance();
    private static final ClientDao clientDao = ClientDao.getInstance();

    private Client savedClient;

    @BeforeAll
    void beforeAll() throws Exception {
        TestDatabaseSetup.createTables();
    }

    @BeforeEach
    void beforeEach() throws Exception {
        TestDatabaseSetup.dropTables();
        savedClient = clientDao.save(new Client("TestUser", "test@example.com", "000"));
    }

    @AfterAll
    static void afterAll() {
        ConnectionManager.closeConnectionPool();
    }

    @Test
    void save() {
        Account account = new Account(savedClient, 100.0, true);
        accountDao.save(account);

        assertTrue(account.getAccountId() > 0, "account_id должен быть сгенерирован");
    }

    @Test
    void findById() {
        Account account = accountDao.save(new Account(savedClient, 200.0, true));
        var found = accountDao.findById(account.getAccountId());

        assertTrue(found.isPresent(), "Аккаунт должен находиться по id");
        assertEquals(200.0, found.get().getBalance());
        assertEquals(savedClient.getClientId(), found.get().getClient().getClientId());
    }

    @Test
    void findAll() {
        accountDao.save(new Account(savedClient, 300.0, true));
        accountDao.save(new Account(savedClient, 400.0, false));

        List<Account> accounts = accountDao.findAll();
        assertEquals(2, accounts.size(), "Должно вернуться два аккаунта");
    }

    @Test
    void update() {
        Account account = accountDao.save(new Account(savedClient, 500.0, true));
        account.setBalance(999.0);
        account.setActive(false);
        accountDao.update(account);

        var updated = accountDao.findById(account.getAccountId()).orElseThrow();
        assertEquals(999.0, updated.getBalance());
        assertFalse(updated.isActive());
    }

    @Test
    void delete() {
        Account account = accountDao.save(new Account(savedClient, 600.0, true));
        boolean deleted = accountDao.delete(account.getAccountId());

        assertTrue(deleted, "Удаление должно вернуть true");
        assertTrue(accountDao.findById(account.getAccountId()).isEmpty());
    }
}