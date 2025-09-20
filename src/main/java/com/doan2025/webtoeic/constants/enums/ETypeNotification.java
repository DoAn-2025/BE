package com.doan2025.webtoeic.constants.enums;

import com.doan2025.webtoeic.exception.WebToeicException;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum ETypeNotification {
    NOTIFICATION(1), EXERCISE(2);

    private final Integer value;

    ETypeNotification(Integer value) {
        this.value = value;
    }

    public static ETypeNotification fromValue(Integer value) {
        for (ETypeNotification item : ETypeNotification.values()) {
            if (Objects.equals(item.getValue(), value)) {
                return item;
            }
        }
        throw new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.STATUS);
    }

    @JsonValue
    public String getName() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }
}
