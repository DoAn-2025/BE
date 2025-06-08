package com.doan2025.webtoeic.constants.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ResponseCode {

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid key", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1002, "Email must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1003, "{entity} is not true", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1005, "You do not have permission", HttpStatus.FORBIDDEN),
    TOKEN_EXPIRED(1006, "{entity} is expired", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALIDATED(1007, "{entity} is invalid", HttpStatus.UNAUTHORIZED),

    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_ROLE(1009, "{entity} is invalid", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1010, "{entity} is invalid", HttpStatus.UNAUTHORIZED),

    EXISTED(1011, "{entity} already exists", HttpStatus.BAD_REQUEST),
    NOT_EXISTED(1012, "{entity} not existed", HttpStatus.NOT_FOUND),
    IS_NULL(1013, "{entity} is null", HttpStatus.BAD_REQUEST),

    SUCCESS (200, "{entity} is successfully", HttpStatus.OK),
    CREATE_SUCCESS(201, "{entity} has been created ", HttpStatus.CREATED),
    UPDATE_SUCCESS(201, "{entity} has been updated ", HttpStatus.OK),
    DELETE_SUCCESS(201, "{entity} has been deleted ", HttpStatus.OK),
    GET_SUCCESS(202, "Get {entity} is successfully", HttpStatus.OK),

    CANNOT_GET(404, "Cannot get {entity}", HttpStatus.NOT_FOUND),
    CANNOT_DELETE(404, "Cannot delete {entity}", HttpStatus.BAD_REQUEST),
    CANNOT_UPDATE(404, "Cannot update {entity}", HttpStatus.BAD_REQUEST),
    CANNOT_CREATE(404, "Cannot create {entity}", HttpStatus.BAD_REQUEST),

    NOT_MATCHED(404, "Not matched {entity}", HttpStatus.BAD_REQUEST),
    ;


    ResponseCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private String message;
    private final HttpStatusCode statusCode;

    public String getMessage(ResponseObject responseObject) {
        return message.replace("{entity}", responseObject.toString());
    }

    public void setMessage(ResponseObject responseObject) {
        this.message = message.replace("{entity}", responseObject.toString());
    }
}
