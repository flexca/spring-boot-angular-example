package com.github.flexca.sbae.backend.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { SbaeException.class })
    protected ResponseEntity<Object> handleFlexCaException(SbaeException sbaeException, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return handleExceptionInternal(sbaeException, ErrorResponse.fromException(sbaeException), headers,
                sbaeException.getType().getHttpStatus(), request);
    }

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleGenericException(Exception exception, WebRequest request) {
        log.error("Unexpected error: {}", exception.getMessage(), exception);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse response = new ErrorResponse();
        response.setType(ErrorType.INTERNAL_SERVER_ERROR);
        response.setCode(ErrorCode.SYSTEM_UNEXPECTED_ERROR);
        response.setMessage("Unexpected error. Check logs for details");
        return handleExceptionInternal(exception, response, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
