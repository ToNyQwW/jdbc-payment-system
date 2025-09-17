package com.ToNyQwW.payment.jdbc.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<E> {

    E save(E entity);

    Optional<E> findById(int id);

    List<E> findAll();

    void update(E entity);

    boolean delete(int id);
}
