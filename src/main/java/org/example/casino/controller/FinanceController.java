package org.example.casino.controller;

import org.example.casino.dto.FinanceResultDtoRequest;
import org.example.casino.dto.FinanceResultDtoResponse;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public interface FinanceController  {


    ResponseEntity<FinanceResultDtoResponse> createIncome(BigDecimal request);

    ResponseEntity<List<FinanceResultDtoResponse>> findAll();

    ResponseEntity<?> deleteAll();

    ResponseEntity<FinanceResultDtoResponse> update(FinanceResultDtoRequest request);

}
