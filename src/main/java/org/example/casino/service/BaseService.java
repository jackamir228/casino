package org.example.casino.service;

import java.util.List;

/**
 * @param <E> request
 * @param <I> response
 */
public interface BaseService<E, I> {
    I create(E request);

    I update(E request);

    List<I> findAll();

    void deleteAll();
}
