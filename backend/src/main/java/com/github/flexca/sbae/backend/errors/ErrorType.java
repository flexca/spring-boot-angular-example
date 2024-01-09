package com.github.flexca.sbae.backend.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ErrorType {

    NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT(HttpStatus.BAD_REQUEST);

    @Getter
    private final HttpStatus httpStatus;
}
