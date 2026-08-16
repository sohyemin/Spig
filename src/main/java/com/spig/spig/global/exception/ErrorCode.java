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
    XSS_INPUT_ERROR(HttpStatus.BAD_REQUEST, "XSS input error"),

    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "File not founded"),
    CHUNK_STORAGE_ERROR(HttpStatus.BAD_REQUEST, "Chunk Upload failed"),
    CHUNK_MERGE_FAIL(HttpStatus.BAD_REQUEST, "Chunk merge failed"),
    CHUNK_FOLDER_DELETE_ERROR(HttpStatus.BAD_REQUEST, "Chunk Temp Folder delete failed");

    private final HttpStatus status;
    private final String message;
}
