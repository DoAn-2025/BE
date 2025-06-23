package com.doan2025.webtoeic.constants.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ECategoryPost {
    TIPS(1),
    EVENT(2),
    EXAM_EXPERIENCE(3),
    GRAMMAR_AND_VOCABULARY(4);
    private final Integer value;

    ECategoryPost(Integer value) {
        this.value = value;
    }
    public int getValue() {return value;}

    @JsonValue
    public String getName() {
        return name().toLowerCase();
    }
    public static ECategoryPost fromValue(Integer value) {
        for (ECategoryPost category : ECategoryPost.values()) {
            if (category.getValue() == value) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid ERole value: " + value);
    }
}
