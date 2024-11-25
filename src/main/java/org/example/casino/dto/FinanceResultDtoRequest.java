package org.example.casino.dto;

import java.math.BigDecimal;

public record FinanceResultDtoRequest(
        BigDecimal income,
        BigDecimal outcome
) {
}
