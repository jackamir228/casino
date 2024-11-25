package org.example.casino.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.casino.dto.FinanceResultDtoRequest;
import org.example.casino.dto.FinanceResultDtoResponse;
import org.example.casino.entity.FinanceResult;
import org.example.casino.exception.EntityCreateException;
import org.example.casino.mapper.FinanceMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FinanceRepositoryImpl {

    private final JdbcTemplate jdbcTemplate;
    private static final int INCOME_INDEX = 0;
    private static final int OUTCOME_INDEX = 1;
    private final FinanceMapper mapper = new FinanceMapper();

    /**
     * Вставляет переданную сумму денег в таблицу {@code finance_result}, создавая запись в колонке income
     * После выполнения вставки создается сущность {@code FinanceResult} с переданным значением дохода
     * Далее создается сущность преобразуется в {@code FinanceResultDtoResponse}
     * <p>
     * Сущность {@code FinanceResultDto} будет HTTP ответом после выполнения данного метода
     *
     * @param requestIncome - Сумма переданного дохода из HTTP запроса, для вставки в {@code finance_result}
     * @return {@code FinanceResultDto} - DTO, представляющие самой,
     * что было вставлено в результате исполнения нашего метода
     * @throws EntityCreateException - ошибка, возникающая при вставке в таблицу {@code finance_result}
     */

    public FinanceResultDtoResponse createIncome(BigDecimal requestIncome) {
        String sql = "INSERT INTO finance_result(income) VALUES (?)";

        try {
            int insertedRows = jdbcTemplate.update(sql, ps -> ps.setBigDecimal(1, requestIncome));

            FinanceResult financeResult = new FinanceResult(requestIncome);
            log.info("Number of rows inserted into finance_result {}", insertedRows);

            return mapper.convertEntityToResponse(financeResult);
        } catch (DataAccessException e) {
            log.error("Error inserting to finance_result", e);
            throw new EntityCreateException("Error inserting into finance_result", e);
        }
    }

    /**
     * Создается запись {@code outcome} в таблице {@code finance_result} по значению {@code income}
     *
     * @param createOutcomeDto - DTO, с параметрами {@code income} {@code outcome}
     * @return - обновленную сущность {@code finance_result}
     */
    public FinanceResultDtoResponse createOutcomeByCurrentIncome(FinanceResultDtoRequest createOutcomeDto) {
        String sql = "UPDATE finance_result SET outcome = ? WHERE income = ?";

        jdbcTemplate.update(sql,
                ps -> {
                    ps.setBigDecimal(1, createOutcomeDto.outcome());
                    ps.setBigDecimal(2, createOutcomeDto.income());
                });
        return new FinanceResultDtoResponse(createOutcomeDto.income(), createOutcomeDto.outcome());
    }

    public int update(FinanceResult financeEntity, List<BigDecimal> updatedValues) {
        String sqlUpdate = "UPDATE finance_result SET income = ?, outcome = ? WHERE income = ? AND outcome = ?;";

        int updateRows = jdbcTemplate.update(sqlUpdate,
                ps -> {
                    ps.setBigDecimal(1, updatedValues.get(INCOME_INDEX));
                    ps.setBigDecimal(2, updatedValues.get(OUTCOME_INDEX));
                    ps.setBigDecimal(3, financeEntity.getIncome());
                    ps.setBigDecimal(4, financeEntity.getOutcome());
                }
        );
        log.info("put to finance_result, rows affected - {}, new income - {}, new outcome - {} ",
                updateRows,
                updatedValues.get(INCOME_INDEX),
                updatedValues.get(OUTCOME_INDEX));
        return updateRows;
    }

    /**
     * @return Возвращает запись из таблицы {@code finance_result}
     */

    public List<FinanceResult> findAll() {
        String sqlQuery = "select * from finance_result LIMIT 1";
        List<FinanceResult> findAllList = jdbcTemplate.query(sqlQuery, this.mapper::financeResultRowMapper);
        log.info("Object found in finance_result table {}", findAllList);
        return findAllList;
    }

    /**
     * Удаляет все записи из таблицы {@code finance_result}
     */

    public void deleteAll() {
        String sql = "TRUNCATE TABLE finance_result";
        jdbcTemplate.update(sql);
        log.info("table finance_result has been truncated");
    }

    /**
     * @return - Возвращает конкретную запись из таблицы {@code finance_result}
     */
    public int patch(BigDecimal updateMoney, String columnName) {
        String sql = "UPDATE casino.public.finance_result SET %s = ?;".formatted(columnName);
        return jdbcTemplate.update(sql, ps -> ps.setBigDecimal(1, updateMoney));
    }

    /**
     * находит нужную колонку
     */
    public List<BigDecimal> findColumn(String columnName) {
        String sql = "SELECT %s FROM finance_result".formatted(columnName);
        return jdbcTemplate.query(sql, (resultSet, rwNum) -> resultSet.getBigDecimal(columnName));
    }
}
