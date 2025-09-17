package com.ToNyQwW.payment.jdbc.dao;

import com.ToNyQwW.payment.jdbc.dao.util.TestDatabaseSetup;
import com.ToNyQwW.payment.jdbc.entity.*;
import com.ToNyQwW.payment.jdbc.entity.Order;
import com.ToNyQwW.payment.jdbc.util.ConnectionManager;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentDaoTest {

    private static final PaymentDao paymentDao = PaymentDao.getInstance();
    private static final ClientDao clientDao = ClientDao.getInstance();
    private static final AccountDao accountDao = AccountDao.getInstance();
    private static final OrderDao orderDao = OrderDao.getInstance();

    private Client savedClient;
    private Account fromAccount;
    private Account toAccount;
    private Order order;

    @BeforeEach
    void beforeEach() throws Exception {
        TestDatabaseSetup.createTables();
        savedClient = clientDao.save(new Client("PaymentUser", "payment@example.com", "999"));
        fromAccount = accountDao.save(new Account(savedClient, 1000.0, true));
        toAccount = accountDao.save(new Account(savedClient, 2000.0, true));
        order = orderDao.save(new Order(savedClient, 500.0, OrderStatus.NEW));
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
        Payment payment = new Payment(fromAccount, toAccount, order, 300.0, LocalDateTime.now());
        paymentDao.save(payment);

        assertTrue(payment.getPaymentId() > 0, "payment_id должен быть сгенерирован");
    }

    @Test
    void findById() {
        Payment payment = paymentDao.save(
                new Payment(fromAccount, toAccount, order, 400.0, LocalDateTime.now())
        );
        var found = paymentDao.findById(payment.getPaymentId());

        assertTrue(found.isPresent(), "Платеж должен находиться по id");
        assertEquals(400.0, found.get().getAmount());
        assertEquals(fromAccount.getAccountId(), found.get().getFromAccount().getAccountId());
        assertEquals(toAccount.getAccountId(), found.get().getToAccount().getAccountId());
        assertEquals(order.getOrderId(), found.get().getOrder().getOrderId());
    }

    @Test
    void findAll() {
        paymentDao.save(new Payment(fromAccount, toAccount, order, 100.0, LocalDateTime.now()));
        paymentDao.save(new Payment(fromAccount, toAccount, order, 200.0, LocalDateTime.now()));

        List<Payment> payments = paymentDao.findAll();
        assertEquals(2, payments.size(), "Должно вернуться два платежа");
    }

    @Test
    void update() {
        Payment payment = paymentDao.save(
                new Payment(fromAccount, toAccount, order, 250.0, LocalDateTime.now())
        );
        payment.setAmount(999.0);
        paymentDao.update(payment);

        var updated = paymentDao.findById(payment.getPaymentId()).orElseThrow();
        assertEquals(999.0, updated.getAmount());
    }

    @Test
    void delete() {
        Payment payment = paymentDao.save(
                new Payment(fromAccount, toAccount, order, 150.0, LocalDateTime.now())
        );
        boolean deleted = paymentDao.delete(payment.getPaymentId());

        assertTrue(deleted, "Удаление должно вернуть true");
        assertTrue(paymentDao.findById(payment.getPaymentId()).isEmpty());
    }
}