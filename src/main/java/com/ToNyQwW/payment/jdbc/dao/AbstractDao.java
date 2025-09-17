package com.ToNyQwW.payment.jdbc.dao;

import com.ToNyQwW.payment.jdbc.exception.DaoException;
import com.ToNyQwW.payment.jdbc.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<E> implements Dao<E> {

    protected abstract String getSaveSql();

    protected abstract String getFindByIdSql();

    protected abstract String getFindAllSql();

    protected abstract String getUpdateSql();

    protected abstract String getDeleteSql();

    protected abstract void setSaveStatement(PreparedStatement preparedStatement, E entity) throws SQLException;

    protected abstract void setUpdateStatement(PreparedStatement preparedStatement, E entity) throws SQLException;

    protected abstract void setGeneratedKey(E entity, ResultSet keys) throws SQLException;

    protected abstract E mapResultSet(ResultSet resultSet) throws SQLException;

    @Override
    public E save(E entity) {
        try (var connection = ConnectionManager.getConnection();
             var preparedStatement
                     = connection.prepareStatement(getSaveSql(), Statement.RETURN_GENERATED_KEYS)) {
            setSaveStatement(preparedStatement, entity);
            preparedStatement.executeUpdate();
            try (var keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    setGeneratedKey(entity, keys);
                }
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<E> findById(int id) {
        try (var connection = ConnectionManager.getConnection()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<E> findById(int id, Connection connection) {
        try (var prepareStatement = connection.prepareStatement(getFindByIdSql())) {
            prepareStatement.setInt(1, id);
            try (var resultSet = prepareStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSet(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> findAll() {
        try (var connection = ConnectionManager.getConnection();
             var preparedStatement = connection.prepareStatement(getFindAllSql());
             var resultSet = preparedStatement.executeQuery()) {

            List<E> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(mapResultSet(resultSet));
            }
            return list;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(E entity) {
        try (var connection = ConnectionManager.getConnection();
             var preparedStatement = connection.prepareStatement(getUpdateSql())) {
            setUpdateStatement(preparedStatement, entity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        try (var connection = ConnectionManager.getConnection();
             var preparedStatement = connection.prepareStatement(getDeleteSql())) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
