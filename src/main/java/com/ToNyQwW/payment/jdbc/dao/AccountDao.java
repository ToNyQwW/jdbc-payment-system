package com.ToNyQwW.payment.jdbc.dao;

import com.ToNyQwW.payment.jdbc.entity.Account;
import com.ToNyQwW.payment.jdbc.exception.DaoException;
import com.ToNyQwW.payment.jdbc.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDao implements Dao<Account> {

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
    public Account save(Account account) {
        try (var connection = ConnectionManager.getConnection();
             var prepareStatement
                     = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement.setInt(1, account.getClient().getClientId());
            prepareStatement.setDouble(2, account.getBalance());
            prepareStatement.setBoolean(3, account.isActive());

            prepareStatement.executeUpdate();

            var generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt("account_id");
                account.setAccountId(id);
            }
            return account;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Account> findById(int id) {
        try (var connection = ConnectionManager.getConnection();
             var prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            prepareStatement.setInt(1, id);

            var resultSet = prepareStatement.executeQuery();
            Account account = null;
            if (resultSet.next()) {
                account = getAccount(resultSet);
            }
            return Optional.ofNullable(account);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Account> findAll() {
        try (var connection = ConnectionManager.getConnection();
             var prepareStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            var resultSet = prepareStatement.executeQuery();
            List<Account> accounts = new ArrayList<>();
            while (resultSet.next()) {
                Account account = getAccount(resultSet);
                accounts.add(account);
            }
            return accounts;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Account getAccount(ResultSet resultSet) {
        try {
            return new Account(
                    resultSet.getInt("account_id"),
                    ClientDao.getInstance()
                            .findById(resultSet.getInt("client_id"),
                                    resultSet.getStatement().getConnection())
                            .orElse(null),
                    resultSet.getDouble("balance"),
                    resultSet.getBoolean("is_active")
            );
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Account account) {
        try (var connection = ConnectionManager.getConnection();
             var prepareStatement = connection.prepareStatement(UPDATE_SQL)) {
            prepareStatement.setInt(1, account.getClient().getClientId());
            prepareStatement.setDouble(2, account.getBalance());
            prepareStatement.setBoolean(3, account.isActive());
            prepareStatement.setInt(4, account.getAccountId());

            prepareStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        try (var connection = ConnectionManager.getConnection();
             var prepareStatement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            prepareStatement.setInt(1, id);

            return prepareStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
