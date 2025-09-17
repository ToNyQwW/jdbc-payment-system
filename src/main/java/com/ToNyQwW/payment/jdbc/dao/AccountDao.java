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
    protected void setSaveStatement(PreparedStatement ps, Account account) throws SQLException {
        ps.setInt(1, account.getClient().getClientId());
        ps.setDouble(2, account.getBalance());
        ps.setBoolean(3, account.isActive());
    }

    @Override
    protected void setUpdateStatement(PreparedStatement ps, Account account) throws SQLException {
        ps.setInt(1, account.getClient().getClientId());
        ps.setDouble(2, account.getBalance());
        ps.setBoolean(3, account.isActive());
        ps.setInt(4, account.getAccountId());
    }

    @Override
    protected void setGeneratedKey(Account account, ResultSet keys) throws SQLException {
        account.setAccountId(keys.getInt(1));
    }

    @Override
    protected Account mapResultSet(ResultSet rs) throws SQLException {
        var client = ClientDao.getInstance().findById(rs.getInt("client_id")).orElse(null);
        return new Account(
                rs.getInt("account_id"),
                client,
                rs.getDouble("balance"),
                rs.getBoolean("is_active")
        );
    }
}
