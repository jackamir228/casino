package org.example.casino.entity;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@ToString
public class FinanceResult {
    private BigDecimal income;
    private BigDecimal outcome;

    public FinanceResult() {
    }

    public FinanceResult(BigDecimal income) {
        this.income = income;
    }

    public FinanceResult(BigDecimal income, BigDecimal outcome) {
        this.income = income;
        this.outcome = outcome;
    }
}
