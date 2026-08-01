package com.spig.spig.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "Room not founded"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"),
    ROOM_NOT_AUTHORIZED(HttpStatus.NOT_FOUND, "Room not authorized"),
    XSS_INPUT_ERROR(HttpStatus.BAD_REQUEST, "XSS input error");

    private final HttpStatus status;
    private final String message;
}
