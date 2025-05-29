package com.doan2025.webtoeic.constants.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EGender {
    MALE(1, "MALE"), FEMALE(2, "FEMALE");

    private final int value;
    private final String name;
    EGender(int value, String name) {
        this.value = value;
        this.name = name;
    }
    @JsonValue
    public int getValue() {return value;}

    @JsonValue
    public String getName() {return name;}
}
