package com.doan2025.webtoeic.constants.enums;

import com.doan2025.webtoeic.exception.WebToeicException;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ECategoryCourse {

    TIPS(1),
    EVENT(2),
    EXAM_EXPERIENCE(3),
    GRAMMAR_AND_VOCABULARY(4);
    private final Integer value;

    ECategoryCourse(Integer value) {
        this.value = value;
    }
    public int getValue() {return value;}

    @JsonValue
    public String getName() {
        return name().toLowerCase();
    }
    public static ECategoryCourse fromValue(Integer value) {
        for (ECategoryCourse category : ECategoryCourse.values()) {
            if (category.getValue() == value) {
                return category;
            }
        }
        throw new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.CATEGORY);
    }
}
