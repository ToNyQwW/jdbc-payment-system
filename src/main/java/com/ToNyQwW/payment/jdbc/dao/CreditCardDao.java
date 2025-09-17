package com.ToNyQwW.payment.jdbc.dao;

import com.ToNyQwW.payment.jdbc.entity.CreditCard;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditCardDao extends AbstractDao<CreditCard> {

    private static final CreditCardDao INSTANCE = new CreditCardDao();

    private static final String SAVE_SQL = """
            INSERT INTO credit_card
            (account_id, card_number, credit_limit, balance, is_blocked)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT card_id,
                   account_id,
                   card_number,
                   credit_limit,
                   balance,
                   is_blocked
            FROM credit_card
            WHERE card_id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT card_id,
                   account_id,
                   card_number,
                   credit_limit,
                   balance,
                   is_blocked
            FROM credit_card
            """;

    private static final String UPDATE_SQL = """
            UPDATE credit_card
            SET account_id = ?,
                card_number = ?,
                credit_limit = ?,
                balance = ?,
                is_blocked = ?
            WHERE card_id = ?
            """;

    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM credit_card
            WHERE card_id = ?
            """;

    private CreditCardDao() {

    }

    public static CreditCardDao getInstance() {
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
    protected void setSaveStatement(PreparedStatement preparedStatement, CreditCard creditCard) throws SQLException {
        preparedStatement.setInt(1, creditCard.getAccount().getAccountId());
        preparedStatement.setString(2, creditCard.getCardNumber());
        preparedStatement.setDouble(3, creditCard.getCreditLimit());
        preparedStatement.setDouble(4, creditCard.getBalance());
        preparedStatement.setBoolean(5, creditCard.isBlocked());
    }

    @Override
    protected void setUpdateStatement(PreparedStatement preparedStatement, CreditCard creditCard) throws SQLException {
        setSaveStatement(preparedStatement, creditCard);
        preparedStatement.setInt(6, creditCard.getCardId());
    }

    @Override
    protected void setGeneratedKey(CreditCard creditCard, ResultSet keys) throws SQLException {
        creditCard.setCardId(keys.getInt(1));
    }

    @Override
    protected CreditCard mapResultSet(ResultSet resultSet) throws SQLException {
        var account = AccountDao.getInstance()
                .findById(resultSet.getInt("account_id"), resultSet.getStatement().getConnection())
                .orElse(null);
        return new CreditCard(
                resultSet.getInt("card_id"),
                account,
                resultSet.getString("card_number"),
                resultSet.getDouble("credit_limit"),
                resultSet.getDouble("balance"),
                resultSet.getBoolean("is_blocked")
        );
    }
}
