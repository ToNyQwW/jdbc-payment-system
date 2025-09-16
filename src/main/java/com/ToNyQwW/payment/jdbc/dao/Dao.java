package com.ToNyQwW.payment.jdbc.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<E> {

    boolean delete(int id);

    E save(E entity);

    void update(E entity);

    Optional<E> findById(int id);

    List<E> findAll();
}
