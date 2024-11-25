package org.example.casino.repository;

import java.util.List;
import java.util.Optional;

/**
 * @param <T> body
 * @param <E> request
 */
public interface BasicRepository<T, E> {
    T create(E e);

    T update(T t);

    List<T> findAll();

    void deleteAll();
}
