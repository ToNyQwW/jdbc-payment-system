package com.ToNyQwW.payment.jdbc.dao;

import com.ToNyQwW.payment.jdbc.entity.Order;
import com.ToNyQwW.payment.jdbc.entity.OrderStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDao extends AbstractDao<Order> {

    private static final OrderDao INSTANCE = new OrderDao();

    private static final String SAVE_SQL = """
            INSERT INTO orders
            (client_id, amount, status)
            VALUES (?, ?, ?)
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT order_id,
                   client_id,
                   amount,
                   status
            FROM orders
            WHERE order_id = ?;
            """;

    private static final String FIND_ALL_SQL = """
            SELECT order_id,
                   client_id,
                   amount,
                   status
            FROM orders
            """;

    private static final String UPDATE_SQL = """
            UPDATE orders
            SET client_id = ?,
                amount = ?,
                status = ?
            WHERE order_id = ?;
            """;

    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM orders
            WHERE order_id = ?;
            """;

    private OrderDao() {

    }

    public static OrderDao getInstance() {
        return INSTANCE;
    }

    @Override
    protected String getSaveSql() {
        return SAVE_SQL;
    }

    @Override
    protected String getFindByIdSql() {
        return FIND_BY_ID_SQL;
    }

    @Override
    protected String getFindAllSql() {
        return FIND_ALL_SQL;
    }

    @Override
    protected String getUpdateSql() {
        return UPDATE_SQL;
    }

    @Override
    protected String getDeleteSql() {
        return DELETE_BY_ID_SQL;
    }

    @Override
    protected void setSaveStatement(PreparedStatement preparedStatement, Order order) throws SQLException {
        preparedStatement.setInt(1, order.getClient().getClientId());
        preparedStatement.setDouble(2, order.getAmount());
        preparedStatement.setString(3, order.getStatus().toString());
    }

    @Override
    protected void setUpdateStatement(PreparedStatement preparedStatement, Order order) throws SQLException {
        setSaveStatement(preparedStatement, order);
        preparedStatement.setInt(4, order.getOrderId());
    }

    @Override
    protected void setGeneratedKey(Order order, ResultSet keys) throws SQLException {
        order.setOrderId(keys.getInt(1));
    }

    @Override
    protected Order mapResultSet(ResultSet resultSet) throws SQLException {
        var client = ClientDao.getInstance()
                .findById(resultSet.getInt("client_id"), resultSet.getStatement().getConnection())
                .orElse(null);
        return new Order(
                resultSet.getInt("order_id"),
                client,
                resultSet.getDouble("amount"),
                OrderStatus.valueOf(resultSet.getString("status"))
        );
    }
}
