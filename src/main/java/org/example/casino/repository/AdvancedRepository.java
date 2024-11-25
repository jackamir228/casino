package org.example.casino.repository;

import java.util.Optional;

public interface AdvancedRepository<T> {
    void deleteById(Long id);

    Optional<T> findById(Long id);
}
