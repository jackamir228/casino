package org.example.casino.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.casino.dto.SlotDtoResponse;
import org.example.casino.entity.Slot;
import org.example.casino.enums.ResultSymbolCombination;
import org.example.casino.repository.SlotRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.example.casino.enums.ResultSymbolCombination.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService<SlotDtoResponse> {

    private final List<String> gamingSlot = List.of("A", "F", "D");

    private final SlotRepository slotRepository;

    private final Random random = new Random();

    public ResultSymbolCombination createSlot() {
        String result = generatedResult();
        switch (result) {
            case "AAA" -> {
                log.info("Symbol combination is {}, you are win is 10", "AAA");
                return TEN;
            }
            case "FFF" -> {
                log.info("Symbol combination is {}, you are win is 20", "FFF");
                return TWENTY;
            }
            case "DDD" -> {
                log.info("Symbol combination is {}, you are win is 50", "DDD");
                return FIFTY;
            }
            case "AFD" -> {
                return FREE_MOVE;
            }
            default -> {
                log.info("You winning is null {}", result);
                return YOU_WIN_IS_NULL;
            }
        }
    }

    public List<SlotDtoResponse> findDesc() {
        return slotRepository.findHistorySlots();
    }

    private String generatedResult() {
        List<String> symbols = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            String generatedSymbol = gamingSlot.get(generatedIndex());
            log.info("symbol is {}", generatedSymbol);
            symbols.add(generatedSymbol);
        }
        slotRepository.create(new Slot(null, symbols.get(0), symbols.get(1), symbols.get(2)));
        String symbolCombination = symbols.get(0) + symbols.get(1) + symbols.get(2);
        log.info("symbol combination is - {}", symbolCombination);
        return symbolCombination;
    }

    private int generatedIndex() {
        return random.nextInt(3);
    }

    @Override
    public List<SlotDtoResponse> findAll() {
        return slotRepository.findAll();
    }

    @Override
    public Optional<Slot> findById(Long id) {
        return slotRepository.findById(id);
    }

    @Override
    public void deleteAll() {
        slotRepository.deleteAll();
    }

    @Override
    public void deleteById(Long id) {
        slotRepository.deleteById(id);
    }
}
