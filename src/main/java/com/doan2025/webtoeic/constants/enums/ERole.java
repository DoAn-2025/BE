package com.doan2025.webtoeic.constants.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ERole {
    MANAGER(1, "MANAGER"),
    CONSULTANT(2, "CONSULTANT"),
    TEACHER(3, "TEACHER"),
    STUDENT(4, "STUDENT");

    private final Integer value;
    private final String code;

    ERole(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    @JsonValue
    public int getValue() {return value;}

    @JsonValue
    public String getCode() {return code;}

    public static ERole fromValue(Integer value) {
        for (ERole role : ERole.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid ERole value: " + value);
    }


}
