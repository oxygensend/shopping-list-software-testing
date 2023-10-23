package com.oxygensend.backend.application.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExceptionResponseTest {

    @Test
    public void testExceptionResponseWithMessage() {
        // Arrange
        String message = "Test Exception Message";

        // Act
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, message);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, exceptionResponse.getStatus());
        assertEquals(message, exceptionResponse.getMessage());
    }

    @Test
    public void testExceptionResponseWithThrowable() {
        // Arrange
        Throwable exception = new RuntimeException("Test Exception");

        // Act
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exceptionResponse.getStatus());
        assertEquals("Unexpected error", exceptionResponse.getMessage());
        assertEquals("Test Exception", exceptionResponse.getDebugMessage());
    }

    @Test
    public void testAddValidationErrors() {
        // Arrange
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("object", "field", "rejectedValue", false, null, null, "error message"));

        // Act
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, "Validation Failed");
        exceptionResponse.addValidationErrors(fieldErrors);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, exceptionResponse.getStatus());
        assertEquals("Validation Failed", exceptionResponse.getMessage());

        List<SubExceptionResponse> subExceptions = exceptionResponse.getSubExceptions();
        assertEquals(1, subExceptions.size());

        ValidationExceptionResponse subException = (ValidationExceptionResponse) subExceptions.get(0);
        assertEquals("object", subException.getObject());
        assertEquals("field", subException.getField());
        assertEquals("rejectedValue", subException.getRejectedValue());
        assertEquals("error message", subException.getMessage());
    }


}
