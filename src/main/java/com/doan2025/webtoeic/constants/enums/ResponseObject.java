package com.doan2025.webtoeic.constants.enums;

import lombok.Getter;

@Getter
public enum ResponseObject {
    ROLE, USER, PASSWORD,TOKEN, POST,

    USERNAME, EMAIL, IDENTITY_NUMBER, CODE, ID, FILE, URL,

    REGISTER, LOGIN, LOGOUT, REFRESH_TOKEN,VERIFY
    ;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
