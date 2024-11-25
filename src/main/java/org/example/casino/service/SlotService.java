package org.example.casino.service;

import org.example.casino.entity.Slot;

import java.util.List;
import java.util.Optional;

public interface SlotService<SlotDtoResponse> {
    List<SlotDtoResponse> findAll();
    Optional<Slot> findById(Long id);
    void deleteAll();
    void deleteById(Long id);

}
