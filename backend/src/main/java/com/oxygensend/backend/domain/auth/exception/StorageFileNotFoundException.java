package com.oxygensend.backend.domain.auth.exception;

import com.oxygensend.backend.application.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StorageFileNotFoundException extends ApiException {
    public StorageFileNotFoundException(String message) {
        super(message);
    }
}
