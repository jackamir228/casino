package org.example.casino.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlotSqlFieldName {
    FIRST_SYM("first_sym"), SECOND_SYM("second_sym"), THIRD_SYM("third_sym");

    private final String sqlFieldName;

}
