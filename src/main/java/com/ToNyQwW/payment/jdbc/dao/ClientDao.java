package com.ToNyQwW.payment.jdbc.dao;


import com.ToNyQwW.payment.jdbc.entity.Client;
import com.ToNyQwW.payment.jdbc.exception.DaoException;
import com.ToNyQwW.payment.jdbc.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDao implements Dao<Client> {

    private static final ClientDao INSTANCE = new ClientDao();

    private static final String FIND_BY_ID_SQL = """
            SELECT client_id,
                   full_name,
                   email,
                   phone
            FROM client
            WHERE client_id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT client_id,
                   full_name,
                   email,
                   phone
            FROM client
            """;

    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM client
            WHERE client_id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO client
            (full_name, email, phone)
            VALUES (?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE client
            SET full_name = ?,
                email = ?,
                phone = ?
            WHERE client_id = ?
            """;

    private ClientDao() {
    }

    public static ClientDao getInstance() {
        return INSTANCE;
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

    @Override
    public Client save(Client client) {
        try (var connection = ConnectionManager.getConnection();
             var prepareStatement
                     = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement.setString(1, client.getFullName());
            prepareStatement.setString(2, client.getEmail());
            prepareStatement.setString(3, client.getPhone());

            prepareStatement.executeUpdate();

            var generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt("client_id");
                client.setClientId(id);
            }
            return client;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Client client) {
        try (var connection = ConnectionManager.getConnection();
             var prepareStatement = connection.prepareStatement(UPDATE_SQL)) {
            prepareStatement.setString(1, client.getFullName());
            prepareStatement.setString(2, client.getEmail());
            prepareStatement.setString(3, client.getPhone());
            prepareStatement.setInt(4, client.getClientId());

            prepareStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Client> findById(int id, Connection connection) {
        try (var prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            prepareStatement.setInt(1, id);

            var resultSet = prepareStatement.executeQuery();
            Client client = null;
            if (resultSet.next()) {
                client = getClient(resultSet);
            }
            return Optional.ofNullable(client);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Client> findById(int id) {
        try (var connection = ConnectionManager.getConnection()) {
            return findById(id, connection);
        } catch (
                SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Client> findAll() {
        try (var connection = ConnectionManager.getConnection();
             var prepareStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            var resultSet = prepareStatement.executeQuery();
            List<Client> clients = new ArrayList<>();
            while (resultSet.next()) {
                Client client = getClient(resultSet);
                clients.add(client);
            }
            return clients;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private static Client getClient(ResultSet resultSet) throws SQLException {
        return new Client(
                resultSet.getInt("client_id"),
                resultSet.getString("full_name"),
                resultSet.getString("email"),
                resultSet.getString("phone")
        );
    }
}
