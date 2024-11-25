package org.example.casino.mapper;

/**
 * @param <T> entity
 * @param <E> request
 * @param <I> response
 */
public interface Mapper<T, E, I> {

    T convertRequestToEntity(E request);

    T convertResponseToEntity(I response);

    I convertEntityToResponse(T entity);
}
