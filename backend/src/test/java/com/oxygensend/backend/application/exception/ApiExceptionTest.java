package com.oxygensend.backend.application.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiExceptionTest {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private static class CustomApiException extends ApiException {
        public CustomApiException(String message) {
            super(message);
        }
    }

    @Test
    public void testGetStatusCode() {
        // Arrange
        ResponseStatus responseStatus = mock(ResponseStatus.class);
        when(responseStatus.value()).thenReturn(HttpStatus.BAD_REQUEST);

        CustomApiException customApiException = new CustomApiException("Test exception message");

        // Act
        HttpStatus statusCode = customApiException.getStatusCode();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, statusCode);
    }
}
