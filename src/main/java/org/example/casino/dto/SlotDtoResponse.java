package org.example.casino.dto;

public record SlotDtoResponse(
        Long id,
        String firstSymbol,
        String secondSymbol,
        String thirdSymbol
) {
}
