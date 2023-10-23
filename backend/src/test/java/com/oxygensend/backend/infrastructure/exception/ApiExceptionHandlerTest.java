package com.oxygensend.backend.infrastructure.exception;

import com.oxygensend.backend.application.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApiExceptionHandlerTest {

    @InjectMocks
    private ApiExceptionHandler apiExceptionHandler;


    @Test
    public void testHandleHttpMessageNotReadableException() {
        // Arrange
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Malformed JSON request", mock(HttpInputMessage.class));

        // Act
        ResponseEntity<Object> responseEntity = apiExceptionHandler.handleHttpMessageNotReadable(exception, null, null, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testHandleCustomException() {
        // Arrange
        ApiException exception = new ApiException("Custom Exception Message") {
            @Override
            public HttpStatus getStatusCode() {
                return HttpStatus.BAD_REQUEST;
            }
        };

        // Act
        ResponseEntity<Object> responseEntity = apiExceptionHandler.handleCustomException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testHandleMethodArgumentNotValidException() {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        WebRequest request = mock(WebRequest.class);

        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(mock(FieldError.class)));
        when(bindingResult.getGlobalErrors()).thenReturn(List.of(mock(ObjectError.class)));

        // Act
        ResponseEntity<Object> responseEntity = apiExceptionHandler.handleMethodArgumentNotValid(exception, null, null, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testHandleBadCredentialsException() {
        // Arrange
        BadCredentialsException exception = new BadCredentialsException("Bad Credentials");

        // Act
        ResponseEntity<Object> responseEntity = apiExceptionHandler.handleBadCredentialsException(exception);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        // Add more assertions as needed
    }
}
