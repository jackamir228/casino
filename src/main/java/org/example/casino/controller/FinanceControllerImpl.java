package org.example.casino.controller;

import lombok.RequiredArgsConstructor;
import org.example.casino.dto.FinanceResultDtoRequest;
import org.example.casino.dto.FinanceResultDtoResponse;
import org.example.casino.service.FinanceServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequiredArgsConstructor
@RequestMapping("finance")
public class FinanceControllerImpl implements FinanceController {

    private final FinanceServiceImpl financeService;

    @PostMapping("/create-income")
    @Override
    public ResponseEntity<FinanceResultDtoResponse> createIncome(@RequestParam BigDecimal request) {
        return ResponseEntity.status(CREATED).body(financeService.createIncome(request));
    }

    @PostMapping("create-outcome-by-income")
    public ResponseEntity<FinanceResultDtoResponse> createOutcomeByIncome(@RequestBody FinanceResultDtoRequest request) {
        return ResponseEntity.status(CREATED).body(financeService.createOutcomeByIncome(request));
    }

    @PutMapping("/update")
    @Override
    public ResponseEntity<FinanceResultDtoResponse> update(@RequestBody FinanceResultDtoRequest request) {
        return ResponseEntity.status(OK).body(financeService.update(request));
    }

    @PatchMapping("/patch-outcome")
    public ResponseEntity<FinanceResultDtoResponse> patchOutcome(@RequestParam BigDecimal requestOutcome) {
        return ResponseEntity.status(OK).body(financeService.patchOutcome(requestOutcome));
    }

    @PatchMapping("/patch-income")
    public ResponseEntity<?> patchIncome(@RequestParam BigDecimal requestIncome) {
        return ResponseEntity.status(OK).body(financeService.patchIncome(requestIncome));
    }

    @GetMapping("/find-all")
    @Override
    public ResponseEntity<List<FinanceResultDtoResponse>> findAll() {
        return ResponseEntity.status(OK).body(financeService.findAll());
    }

    @GetMapping("/find-income")
    public ResponseEntity<BigDecimal> findIncome() {
       return ResponseEntity.status(OK).body(financeService.findIncome());
    }

    @GetMapping("/find-outcome")
    public ResponseEntity<BigDecimal> findOutcome() {
        return ResponseEntity.status(OK).body(financeService.findOutcome());
    }

    @DeleteMapping("/delete-all")
    @Override
    public ResponseEntity<?> deleteAll() {
        financeService.deleteAll();
        return ResponseEntity.status(OK).build();
    }
}
