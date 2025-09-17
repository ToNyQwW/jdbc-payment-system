package com.ToNyQwW.payment.jdbc.dao;

import com.ToNyQwW.payment.jdbc.entity.Payment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PaymentDao extends AbstractDao<Payment> {

    private static final PaymentDao INSTANCE = new PaymentDao();

    private static final String SAVE_SQL = """
            INSERT INTO payment
            (from_account, to_account, order_id, amount, created_at)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT payment_id,
                   from_account,
                   to_account,
                   order_id,
                   amount,
                   created_at
            FROM payment
            WHERE payment_id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT payment_id,
                   from_account,
                   to_account,
                   order_id,
                   amount,
                   created_at
            FROM payment
            """;

    private static final String UPDATE_SQL = """
            UPDATE payment
            SET from_account = ?,
                to_account = ?,
                order_id = ?,
                amount = ?,
                created_at = ?
            WHERE payment_id = ?
            """;

    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM payment
            WHERE payment_id = ?
            """;

    private PaymentDao() {
    }

    public static PaymentDao getInstance() {
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
    protected void setSaveStatement(PreparedStatement preparedStatement, Payment payment) throws SQLException {
        preparedStatement.setInt(1, payment.getFromAccount().getAccountId());
        preparedStatement.setInt(2, payment.getToAccount().getAccountId());
        preparedStatement.setInt(3, payment.getOrder().getOrderId());
        preparedStatement.setDouble(4, payment.getAmount());
        preparedStatement.setTimestamp(5, Timestamp.valueOf(payment.getCreatedAt()));
    }

    @Override
    protected void setUpdateStatement(PreparedStatement preparedStatement, Payment payment) throws SQLException {
        setSaveStatement(preparedStatement, payment);
        preparedStatement.setInt(6, payment.getPaymentId());
    }

    @Override
    protected void setGeneratedKey(Payment payment, ResultSet keys) throws SQLException {
        payment.setPaymentId(keys.getInt("payment_id"));
    }

    @Override
    protected Payment mapResultSet(ResultSet resultSet) throws SQLException {
        var fromAccount = AccountDao.getInstance()
                .findById(resultSet.getInt("from_account"), resultSet.getStatement().getConnection())
                .orElse(null);
        var toAccount = AccountDao.getInstance()
                .findById(resultSet.getInt("to_account"), resultSet.getStatement().getConnection())
                .orElse(null);
        var order = OrderDao.getInstance()
                .findById(resultSet.getInt("order_id"), resultSet.getStatement().getConnection())
                .orElse(null);
        return new Payment(
                resultSet.getInt("payment_id"),
                fromAccount,
                toAccount,
                order,
                resultSet.getDouble("amount"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
