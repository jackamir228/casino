package org.example.casino.dto;

import java.math.BigDecimal;
import java.util.List;

public record GameResult(
        BigDecimal playerIncome,
        BigDecimal playerOutcome,
        List<SlotDtoResponse> gameResults
) {
}
