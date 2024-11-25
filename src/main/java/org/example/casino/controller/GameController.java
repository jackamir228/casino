package org.example.casino.controller;

import lombok.RequiredArgsConstructor;
import org.example.casino.dto.SpinSlotResultDtoResponse;
import org.example.casino.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("casino")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @PostMapping("/spin-slot")
    public ResponseEntity<?> spinSlot(@RequestParam BigDecimal requestIncome) {
        SpinSlotResultDtoResponse spinSlotResultDtoResponse = gameService.spinSlot(requestIncome);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(spinSlotResultDtoResponse);
    }

    @GetMapping("/history")
    public ResponseEntity<?> history() {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.history());
    }

// 4 Если выпадает A F D - транзакция откатывается. Возвращается http ответ: Бесплатный ход.
// 5 Если не A F D и выпадает комбинация, то в income заносится сумма выигрыша. Возвращается http ответ: Вы выиграли - {сумма выигрыша}.
// 6. Если комбинация не выпадает. Возвращается http ответ: Вы ничего не выиграли.
//
//Создать endpoint /history. Данный метод возвращает ответ в следующем формате:
//{
// "playerIncome": значение income из finance_result,
// "playerOutcome": значение outcome из finance_result,
// "game_history": [результаты последних 5 игр]
//
}

