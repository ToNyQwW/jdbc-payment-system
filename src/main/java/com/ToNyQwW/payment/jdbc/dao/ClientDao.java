package com.ToNyQwW.payment.jdbc.dao;

import com.ToNyQwW.payment.jdbc.entity.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDao extends AbstractDao<Client> {

    private static final ClientDao INSTANCE = new ClientDao();

    private static final String SAVE_SQL = """
            INSERT INTO client
            (full_name, email, phone)
            VALUES (?, ?, ?)
            """;

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

    private static final String UPDATE_SQL = """
            UPDATE client
            SET full_name = ?,
                email = ?,
                phone = ?
            WHERE client_id = ?
            """;

    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM client
            WHERE client_id = ?
            """;

    private ClientDao() {
    }

    public static ClientDao getInstance() {
        return INSTANCE;
    }

    @Override
    protected String getSaveSql() {
        return SAVE_SQL;
    }

    @Override
    protected String getFindByIdSql(){
        return FIND_BY_ID_SQL;
    }

    @Override
    protected String getFindAllSql(){
        return FIND_ALL_SQL;
    }

    @Override
    protected String getUpdateSql(){
        return UPDATE_SQL;
    }

    @Override
    protected String getDeleteSql(){
        return DELETE_BY_ID_SQL;
    }

    @Override
    protected void setSaveStatement(PreparedStatement preparedStatement, Client client) throws SQLException {
        preparedStatement.setString(1, client.getFullName());
        preparedStatement.setString(2, client.getEmail());
        preparedStatement.setString(3, client.getPhone());
    }

    @Override
    protected void setUpdateStatement(PreparedStatement preparedStatement, Client client) throws SQLException {
        preparedStatement.setString(1, client.getFullName());
        preparedStatement.setString(2, client.getEmail());
        preparedStatement.setString(3, client.getPhone());
        preparedStatement.setInt(4, client.getClientId());
    }

    @Override
    protected void setGeneratedKey(Client client, ResultSet keys) throws SQLException {
        client.setClientId(keys.getInt(1));
    }

    @Override
    protected Client mapResultSet(ResultSet resultSet) throws SQLException {
        return new Client(
                resultSet.getInt("client_id"),
                resultSet.getString("full_name"),
                resultSet.getString("email"),
                resultSet.getString("phone")
        );
    }
}