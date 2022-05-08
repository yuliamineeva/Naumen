package ru.naumen.javakids.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    NOT_STARTED("Не начиналась"),
    IN_PROCESS("В процессе"),
    FINISHED("Завершена");

    private final String description;

    @Override
    public String toString() {
        return description;
    }
}
