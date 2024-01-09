package com.github.flexca.sbae.backend.errors;

import lombok.Getter;

public class SbaeException extends RuntimeException {

    @Getter
    private final ErrorType type;
    @Getter
    private final ErrorCode code;
    @Getter
    private String field;

    public SbaeException(String message, ErrorType type, ErrorCode code) {
        super(message);
        this.type = type;
        this.code = code;
    }

    public SbaeException(String message, ErrorType type, ErrorCode code, Throwable cause) {
        super(message, cause);
        this.type = type;
        this.code = code;
    }

    public SbaeException(String message, ErrorType type, ErrorCode code, String field) {
        super(message);
        this.type = type;
        this.code = code;
        this.field = field;
    }

    public SbaeException(String message, ErrorType type, ErrorCode code, String field, Throwable cause) {
        super(message, cause);
        this.type = type;
        this.code = code;
        this.field = field;
    }
}
