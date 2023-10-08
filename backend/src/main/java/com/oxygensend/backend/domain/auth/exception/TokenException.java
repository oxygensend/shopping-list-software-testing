package com.oxygensend.backend.domain.auth.exception;

import com.oxygensend.backend.application.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class TokenException extends ApiException {
    public TokenException(String message) {
        super(message);
    }
}
