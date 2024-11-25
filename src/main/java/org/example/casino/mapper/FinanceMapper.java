package org.example.casino.mapper;

import lombok.NoArgsConstructor;
import org.example.casino.dto.FinanceResultDtoRequest;
import org.example.casino.dto.FinanceResultDtoResponse;
import org.example.casino.entity.FinanceResult;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;


public class FinanceMapper implements Mapper<FinanceResult, FinanceResultDtoRequest, FinanceResultDtoResponse>{

    @Override
    public FinanceResult convertRequestToEntity(FinanceResultDtoRequest request) {
        return new FinanceResult(request.income(), request.outcome());
    }

    @Override
    public FinanceResultDtoResponse convertEntityToResponse(FinanceResult entity) {
        return new FinanceResultDtoResponse(entity.getIncome(), entity.getOutcome());
    }

    @Override
    public FinanceResult convertResponseToEntity(FinanceResultDtoResponse response) {
        return new FinanceResult(response.income(), response.outcome());
    }

    public FinanceResult financeResultRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        BigDecimal income = resultSet.getBigDecimal("income");
        BigDecimal outcome = resultSet.getBigDecimal("outcome");
        return new FinanceResult(income, outcome);
    }
}
