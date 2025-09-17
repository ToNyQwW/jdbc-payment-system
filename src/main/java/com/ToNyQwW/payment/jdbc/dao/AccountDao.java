package com.ToNyQwW.payment.jdbc.dao;

import com.ToNyQwW.payment.jdbc.entity.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDao extends AbstractDao<Account> {

    private static final AccountDao INSTANCE = new AccountDao();

    private static final String SAVE_SQL = """
            INSERT INTO account
                (client_id, balance, is_active)
            VALUES (?, ?, ?)
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT account_id,
                   client_id,
                   balance,
                   is_active
            FROM account
            WHERE account_id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT account_id,
                   client_id,
                   balance,
                   is_active
            FROM account
            """;

    private static final String UPDATE_SQL = """
            UPDATE account
            SET client_id = ?,
                balance = ?,
                is_active = ?
            WHERE account_id = ?
            """;

    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM account
            WHERE account_id = ?
            """;

    private AccountDao() {
    }

    public static AccountDao getInstance() {
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
    protected void setSaveStatement(PreparedStatement preparedStatement, Account account) throws SQLException {
        preparedStatement.setInt(1, account.getClient().getClientId());
        preparedStatement.setDouble(2, account.getBalance());
        preparedStatement.setBoolean(3, account.isActive());
    }

    @Override
    protected void setUpdateStatement(PreparedStatement preparedStatement, Account account) throws SQLException {
        setSaveStatement(preparedStatement, account);
        preparedStatement.setInt(4, account.getAccountId());
    }

    @Override
    protected void setGeneratedKey(Account account, ResultSet keys) throws SQLException {
        account.setAccountId(keys.getInt(1));
    }

    @Override
    protected Account mapResultSet(ResultSet resultSet) throws SQLException {
        var client = ClientDao.getInstance()
                .findById(resultSet.getInt("client_id"), resultSet.getStatement().getConnection())
                .orElse(null);
        return new Account(
                resultSet.getInt("account_id"),
                client,
                resultSet.getDouble("balance"),
                resultSet.getBoolean("is_active")
        );
    }
}
