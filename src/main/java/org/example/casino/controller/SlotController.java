package org.example.casino.controller;


import lombok.RequiredArgsConstructor;
import org.example.casino.dto.SlotDtoRequest;
import org.example.casino.dto.SlotDtoResponse;
import org.example.casino.service.SlotServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/slot")
@RequiredArgsConstructor
public class SlotController implements BasicController<SlotDtoResponse, SlotDtoRequest>, AdvancedController{

    private final SlotServiceImpl slotServiceImpl;

    @Override
    @PostMapping("create-slot")
    public ResponseEntity<?> create() {
        return ResponseEntity.status(CREATED).body(slotServiceImpl.createSlot());
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<SlotDtoResponse>> findAll() {
        return ResponseEntity.status(OK).body(slotServiceImpl.findAll());
    }

    //TODO: проверить requestParam в интерфейсе
    @Override
    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(slotServiceImpl.findById(id));
    }

    @Override
    @DeleteMapping("/delete-all")
    public ResponseEntity<?> deleteAll() {
        slotServiceImpl.deleteAll();
       return ResponseEntity.status(OK).build();
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(slotServiceImpl.findById(id));
    }

    //unused methods

    @Override
    public ResponseEntity<SlotDtoResponse> create(SlotDtoRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<SlotDtoResponse> update(SlotDtoRequest request) {
        return null;
    }


    public ResponseEntity<SlotDtoResponse> patchOutcome(SlotDtoRequest request) {
        return null;
    }
}
