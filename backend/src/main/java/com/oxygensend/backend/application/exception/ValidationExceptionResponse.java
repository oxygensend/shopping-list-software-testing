package com.oxygensend.backend.application.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ValidationExceptionResponse implements SubExceptionResponse {
    private String field;
    private Object rejectedValue;
    private final String object;
    private final String message;

    ValidationExceptionResponse(String object, String message) {
        this.object = object;
        this.message = message;
    }

    public ValidationExceptionResponse(String object, String field, Object rejectedValue, String message) {
        this.object = object;
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }

}