package com.github.flexca.sbae.backend.errors;

import lombok.Data;

@Data
public class ErrorResponse {

    private ErrorType type;
    private ErrorCode code;
    private String message;

    public static ErrorResponse fromException(SbaeException exception) {
        ErrorResponse response = new ErrorResponse();
        response.setType(exception.getType());
        response.setCode(exception.getCode());
        response.setMessage(exception.getMessage());
        return response;
    }
}
