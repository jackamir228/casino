package org.example.casino.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slot {
    private Long id;
    private String firstSymbol;
    private String secondSymbol;
    private String thirdSymbol;
}
