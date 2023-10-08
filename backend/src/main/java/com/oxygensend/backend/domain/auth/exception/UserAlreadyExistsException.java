package com.oxygensend.backend.domain.auth.exception;

import com.oxygensend.backend.application.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserAlreadyExistsException extends ApiException {
    public UserAlreadyExistsException() {
        super("User with this email already exists");
    }

}
