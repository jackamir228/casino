package org.example.casino.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface AdvancedController {
    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> deleteById(Long id);
}
