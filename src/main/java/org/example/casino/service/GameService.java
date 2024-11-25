package org.example.casino.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.casino.dto.*;
import org.example.casino.enums.ResultSymbolCombination;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.example.casino.enums.ResultSymbolCombination.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {

    private final SlotServiceImpl slotServiceImpl;
    private final FinanceServiceImpl financeService;

    public SpinSlotResultDtoResponse spinSlot(BigDecimal requestIncome) {
        ResultSymbolCombination symbolCombination = slotServiceImpl.createSlot();

        if (symbolCombination.equals(FREE_MOVE)) {
            return new SpinSlotResultDtoResponse(symbolCombination, null, null);
        } else if (symbolCombination.equals(YOU_WIN_IS_NULL)) {
            FinanceResultDtoResponse response = financeService.findIncome().signum() == 0
                    ? financeService.createIncome(requestIncome)
                    : financeService.patchIncome(requestIncome);
            return new SpinSlotResultDtoResponse(symbolCombination, response.income(), symbolCombination.getWinValue());
        } else if (symbolCombination.equals(TEN) ||
                symbolCombination.equals(TWENTY) ||
                symbolCombination.equals(FIFTY)) {
            FinanceResultDtoResponse responseIncome = financeService.findIncome().signum() == 0
                    ? financeService.createIncome(requestIncome)
                    : financeService.patchIncome(requestIncome);

            FinanceResultDtoResponse response = financeService.findOutcome().signum() == 0
                    ? financeService.createOutcomeByIncome(new FinanceResultDtoRequest(responseIncome.income(), symbolCombination.getWinValue()))
                    : financeService.update((new FinanceResultDtoRequest(responseIncome.income(), symbolCombination.getWinValue())));
            return new SpinSlotResultDtoResponse(symbolCombination, response.income(), response.outcome());
        }
        throw new RuntimeException("Unnamed exception");
    }

    public GameResult history() {
        BigDecimal playerIncome = financeService.findIncome();
        BigDecimal playerOutcome = financeService.findOutcome();
        List<SlotDtoResponse> gameResults = slotServiceImpl.findDesc();
        return new GameResult(playerIncome, playerOutcome, gameResults);
    }

    //Создать endpoint /history. Данный метод возвращает ответ в следующем формате:
//{
// "playerIncome": значение income из finance_result,
// "playerOutcome": значение outcome из finance_result,
// "game_history": [результаты последних 5 игр]
}
