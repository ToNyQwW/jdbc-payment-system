package com.ToNyQwW.payment.jdbc.dao;

import com.ToNyQwW.payment.jdbc.dao.util.TestDatabaseSetup;
import com.ToNyQwW.payment.jdbc.entity.Account;
import com.ToNyQwW.payment.jdbc.entity.Client;
import com.ToNyQwW.payment.jdbc.entity.CreditCard;
import com.ToNyQwW.payment.jdbc.util.ConnectionManager;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreditCardDaoTest {

    private static final CreditCardDao creditCardDao = CreditCardDao.getInstance();
    private static final AccountDao accountDao = AccountDao.getInstance();
    private static final ClientDao clientDao = ClientDao.getInstance();

    private Account savedAccount;

    @BeforeAll
    void beforeAll() throws Exception {
        TestDatabaseSetup.createTables();
    }

    @BeforeEach
    void beforeEach() throws Exception {
        TestDatabaseSetup.dropTables();
        Client client = clientDao.save(new Client("TestUser", "test@example.com", "000"));
        savedAccount = accountDao.save(new Account(client, 1000.0, true));
    }

    @AfterAll
    static void afterAll() {
        ConnectionManager.closeConnectionPool();
    }

    @Test
    void save() {
        CreditCard card = new CreditCard(savedAccount, "1234567890123456", 5000.0, 0.0, false);
        creditCardDao.save(card);

        assertTrue(card.getCardId() > 0, "card_id должен быть сгенерирован");
    }

    @Test
    void findById() {
        CreditCard card = creditCardDao.save(
                new CreditCard(savedAccount, "1111222233334444", 3000.0, 100.0, false)
        );
        var found = creditCardDao.findById(card.getCardId());

        assertTrue(found.isPresent(), "Карта должна находиться по id");
        assertEquals("1111222233334444", found.get().getCardNumber());
        assertEquals(3000.0, found.get().getCreditLimit());
        assertEquals(savedAccount.getAccountId(), found.get().getAccount().getAccountId());
    }

    @Test
    void findAll() {

        creditCardDao.save(new CreditCard(savedAccount, "2222333344445555", 2000.0, 50.0, false));
        var secondAccount = accountDao.save(new Account(savedAccount.getClient(), 1000.0, true));
        creditCardDao.save(new CreditCard(secondAccount, "3333444455556666", 1000.0, 200.0, true));

        List<CreditCard> cards = creditCardDao.findAll();
        assertEquals(2, cards.size(), "Должно вернуться две карты");
    }

    @Test
    void update() {
        CreditCard card = creditCardDao.save(
                new CreditCard(savedAccount, "4444555566667777", 4000.0, 0.0, false)
        );
        card.setBalance(999.0);
        card.setBlocked(true);
        card.setCreditLimit(8000.0);
        creditCardDao.update(card);

        var updated = creditCardDao.findById(card.getCardId()).orElseThrow();
        assertEquals(999.0, updated.getBalance());
        assertTrue(updated.isBlocked());
        assertEquals(8000.0, updated.getCreditLimit());
    }

    @Test
    void delete() {
        CreditCard card = creditCardDao.save(
                new CreditCard(savedAccount, "5555666677778888", 1500.0, 0.0, false)
        );
        boolean deleted = creditCardDao.delete(card.getCardId());

        assertTrue(deleted, "Удаление должно вернуть true");
        assertTrue(creditCardDao.findById(card.getCardId()).isEmpty());
    }
}