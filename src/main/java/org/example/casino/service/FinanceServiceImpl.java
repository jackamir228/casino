package org.example.casino.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.casino.dto.FinanceResultDtoRequest;
import org.example.casino.dto.FinanceResultDtoResponse;
import org.example.casino.entity.FinanceResult;
import org.example.casino.exception.EntityFieldNotFoundException;
import org.example.casino.exception.EntityNotFoundException;
import org.example.casino.exception.EntityUpdateException;
import org.example.casino.mapper.FinanceMapper;
import org.example.casino.repository.FinanceRepositoryImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinanceServiceImpl {

    private final FinanceRepositoryImpl financeRepository;
    private final FinanceMapper mapper = new FinanceMapper();
    private static final int INCOME_INDEX = 0;
    private static final int OUTCOME_INDEX = 1;

    public FinanceResultDtoResponse createOutcomeByIncome(FinanceResultDtoRequest request) {
        return financeRepository.createOutcomeByCurrentIncome(request);
    }

    public FinanceResultDtoResponse createIncome(BigDecimal request) {
        return financeRepository.createIncome(request);
    }

    /**
     * Данный метод прибавляет значения из DTO {@code FinanceResultDtoRequest} к таблице {@code finance_result}
     * Сложение новых данных с текущими(из таблицы {@code finance_result})
     *
     * @param dtoRequest - DTO с полями доход и расход для обновления.
     *                   <p>
     *                   Получаем и проверяем данные из dto, чтобы они не были null.
     *                   Если значения null, то выбрасывается исключение - NullPointerException.
     *                   После этого получаем уже имеющаяся данные и складываем их с данными из dto.
     *                   Отправляем sql запрос на UPDATE, логируем количество измененных строк
     * @return - {@code FinanceResultDtoResponse} DTO с обновленными данными
     * @throws EntityUpdateException - значения из {@code FinanceResultDtoRequest} не валидны
     * @throws EntityUpdateException - если сущность из таблицы {@code finance_result} не была обнаружена
     */
    public FinanceResultDtoResponse update(FinanceResultDtoRequest dtoRequest) {
        //метод проверки dto на null значения
        FinanceResultDtoRequest validateRequest = validateDtoValues(dtoRequest);
        //получение текущих данных
        FinanceResult currentEntity = getCurrentRecords(findAll());
        //вычисление новых значений
        List<BigDecimal> updatedValues = calculateNewValues(currentEntity, validateRequest);
        //выполнение sql запроса
        financeRepository.update(currentEntity, updatedValues);
        //возврат HTTP ответа
        return mapper.convertEntityToResponse(
                new FinanceResult(updatedValues.get(INCOME_INDEX), updatedValues.get(OUTCOME_INDEX))
        );
    }

    public List<FinanceResultDtoResponse> findAll() {
        List<FinanceResult> results = financeRepository.findAll();
        return results.stream().map(this.mapper::convertEntityToResponse).toList();
    }

    public void deleteAll() {
        financeRepository.deleteAll();
    }

    /**
     * Данный метод работает, только если сущность не пустая, в противном случае UPDATE не случится
     *
     * @param requestIncome Количество входящих денег для изменения
     * @return обновлённую сущность
     */
    public FinanceResultDtoResponse patchIncome(BigDecimal requestIncome) {
        BigDecimal newIncome = abstractPatch(requestIncome, "income");
        log.info("patched income");
        FinanceResult currentRecords = getCurrentRecords(findAll());
        return mapper.convertEntityToResponse(currentRecords);
    }

    public FinanceResultDtoResponse patchOutcome(BigDecimal requestOutcome) {
        BigDecimal newOutcome = abstractPatch(requestOutcome, "outcome");
        log.info("patched outcome");
        FinanceResult currentRecords = getCurrentRecords(findAll());
        return mapper.convertEntityToResponse(currentRecords);
    }

    public BigDecimal findIncome() {
        return findColumn("income");
    }

    /**
     * @return - Возвращает весь доход из таблицы {@code finance_result}
     */
    public BigDecimal findOutcome() {
        return findColumn("outcome");
    }

    /**
     * метод для проверки dto для метода update
     */
    private FinanceResultDtoRequest validateDtoValues(FinanceResultDtoRequest dtoRequest) {
        if (dtoRequest.income() == null || dtoRequest.income().compareTo(BigDecimal.ZERO) < 0) {
            log.error("Invalid request: income is - {}, outcome is - {}", dtoRequest.income(), dtoRequest.outcome());
            throw new EntityUpdateException("Update income can not be null or negative value");
        }

        if (dtoRequest.outcome() == null || dtoRequest.outcome().compareTo(BigDecimal.ZERO) < 0) {
            log.error("Invalid request: income is - {}, outcome is - {}", dtoRequest.income(), dtoRequest.outcome());
            throw new EntityUpdateException("Update outcome can not be null or negative value");
        }
        return dtoRequest;
    }

    /**
     * Найти текущие записи
     */
    private FinanceResult getCurrentRecords(List<FinanceResultDtoResponse> tableValues) {

        if (tableValues.isEmpty()) {
            throw new EntityNotFoundException("Entity not found");
        }
        log.info("Object found in finance_result table with income: {}, outcome: {}",
                tableValues.get(0).income(), tableValues.get(0).outcome());

        //Используется первая запись из списка т.к. доходы только один результат
        return mapper.convertResponseToEntity(tableValues.get(0));
    }

    /**
     * @return расчитывает новые значения для update
     */
    private List<BigDecimal> calculateNewValues(FinanceResult financeEntity, FinanceResultDtoRequest request) {
        BigDecimal newIncome = financeEntity.getIncome().add(request.income());
        BigDecimal newOutcome = financeEntity.getOutcome().add(request.outcome());
        return List.of(newIncome, newOutcome);
    }

    /**
     * находит нужную колонку
     */
    private BigDecimal findColumn(String columnName) {
        validateColumnName(columnName);
        List<BigDecimal> query = financeRepository.findColumn(columnName);
        return validateResultList(query);
    }

    private void validateColumnName(String columnName) {
        if (!columnName.equals("income") && !columnName.equals("outcome")) {
            throw new EntityFieldNotFoundException("column name not found");
        }
    }

    /**
     * проверяет на null
     */
    private BigDecimal validateResultList(List<BigDecimal> query) {

        if (query == null || query.isEmpty() || query.get(0) == null) {
            return BigDecimal.ZERO;
        }
        return query.get(0);
    }

    private BigDecimal abstractPatch(BigDecimal requestMoney, String columnName) {
        validateColumnName(columnName);
        FinanceResult currentRecords = getCurrentRecords(findAll());
        BigDecimal newRequestMoney = calculateNewRequestMoney(currentRecords, requestMoney);
        financeRepository.patch(newRequestMoney, columnName);
        return newRequestMoney;
    }

    private BigDecimal calculateNewRequestMoney(FinanceResult financeEntity, BigDecimal requestMoney) {
        return financeEntity.getIncome().add(requestMoney);
    }
}
