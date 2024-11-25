package org.example.casino.dto;

import org.example.casino.enums.ResultSymbolCombination;

import java.math.BigDecimal;

public record SpinSlotResultDtoResponse(ResultSymbolCombination symbols, BigDecimal income, BigDecimal outcome) {
}
