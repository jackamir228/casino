package org.example.casino.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FinanceResultDtoResponse(
        BigDecimal income,
        BigDecimal outcome
) {
}
