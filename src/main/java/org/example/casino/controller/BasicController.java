package org.example.casino.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @param <E> request
 * @param <I> response
 */
public interface BasicController<I, E> {
    ResponseEntity<?> create();

    ResponseEntity<I> create(E request);

    ResponseEntity<List<I>> findAll();

    ResponseEntity<?> deleteAll();

    ResponseEntity<I> update(E request);

//    ResponseEntity<I> patchOutcome(E request);
}
