package com.oxygensend.backend.application.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ToString
@Getter
public class ExceptionResponse {
    private HttpStatus status;
    private final LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    private List<SubExceptionResponse> subExceptions;

    private ExceptionResponse() {
        this.timestamp = LocalDateTime.now();
    }


    public ExceptionResponse(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public ExceptionResponse(HttpStatus status, Throwable exception) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = exception.getLocalizedMessage();
    }

    public ExceptionResponse(HttpStatus status, String message, Throwable exception) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = exception.getLocalizedMessage();
    }

    private void addSubException(SubExceptionResponse subError) {
        if (subExceptions == null) {
            subExceptions = new ArrayList<>();
        }
        subExceptions.add(subError);
    }

    private void addValidationException(String object, String field, Object rejectedValue, String message) {
        addSubException(new ValidationExceptionResponse(object, field, rejectedValue, message));
    }

    private void addValidationException(String object, String message) {
        addSubException(new ValidationExceptionResponse(object, message));
    }

    private void addValidationException(FieldError fieldError) {
        this.addValidationException(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    private void addValidationException(ObjectError objectError) {
        this.addValidationException(
                objectError.getObjectName(),
                objectError.getDefaultMessage());
    }

    public void addValidationException(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationException);
    }


    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationException);
    }

}
