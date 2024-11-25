package org.example.casino.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum ResultSymbolCombination {
    FREE_MOVE(BigDecimal.ZERO, "AFD"),
    YOU_WIN_IS_NULL(BigDecimal.ZERO, null),
    TEN(BigDecimal.TEN, "AAA"),
    TWENTY(BigDecimal.valueOf(20), "FFF"),
    FIFTY(BigDecimal.valueOf(50), "DDD");

    private final BigDecimal winValue;
    private final String symbolCombination;
}
