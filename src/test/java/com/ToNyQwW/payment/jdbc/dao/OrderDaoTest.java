package com.ToNyQwW.payment.jdbc.dao;

import com.ToNyQwW.payment.jdbc.dao.util.TestDatabaseSetup;
import com.ToNyQwW.payment.jdbc.entity.Client;
import com.ToNyQwW.payment.jdbc.entity.Order;
import com.ToNyQwW.payment.jdbc.entity.OrderStatus;
import com.ToNyQwW.payment.jdbc.util.ConnectionManager;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderDaoTest {

    private static final OrderDao orderDao = OrderDao.getInstance();
    private static final ClientDao clientDao = ClientDao.getInstance();

    private Client savedClient;

    @BeforeEach
    void beforeEach() throws Exception {
        TestDatabaseSetup.createTables();
        savedClient = clientDao.save(new Client("OrderUser", "order@example.com", "777"));
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
        Order order = new Order(savedClient, 150.0, OrderStatus.NEW);
        orderDao.save(order);

        assertTrue(order.getOrderId() > 0, "order_id должен быть сгенерирован");
    }

    @Test
    void findById() {
        Order order = orderDao.save(new Order(savedClient, 200.0, OrderStatus.NEW));
        var found = orderDao.findById(order.getOrderId());

        assertTrue(found.isPresent(), "Заказ должен находиться по id");
        assertEquals(200.0, found.get().getAmount());
        assertEquals(OrderStatus.NEW, found.get().getStatus());
        assertEquals(savedClient.getClientId(), found.get().getClient().getClientId());
    }

    @Test
    void findAll() {
        orderDao.save(new Order(savedClient, 300.0, OrderStatus.NEW));
        orderDao.save(new Order(savedClient, 400.0, OrderStatus.PAID));

        List<Order> orders = orderDao.findAll();
        assertEquals(2, orders.size(), "Должно вернуться два заказа");
    }

    @Test
    void update() {
        Order order = orderDao.save(new Order(savedClient, 500.0, OrderStatus.NEW));
        order.setAmount(999.0);
        order.setStatus(OrderStatus.CANCELLED);
        orderDao.update(order);

        var updated = orderDao.findById(order.getOrderId()).orElseThrow();
        assertEquals(999.0, updated.getAmount());
        assertEquals(OrderStatus.CANCELLED, updated.getStatus());
    }

    @Test
    void delete() {
        Order order = orderDao.save(new Order(savedClient, 600.0, OrderStatus.NEW));
        boolean deleted = orderDao.delete(order.getOrderId());

        assertTrue(deleted, "Удаление должно вернуть true");
        assertTrue(orderDao.findById(order.getOrderId()).isEmpty());
    }
}