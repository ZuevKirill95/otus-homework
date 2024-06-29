package ru.otus;

public enum NominalType {
    FIFTY(50),
    ONE_HUNDRED(100),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1_000),
    FIVE_THOUSAND(5_000);

    final int value;

    NominalType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
