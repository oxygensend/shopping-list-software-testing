package com.oxygensend.backend.domain.auth.exception;

import com.oxygensend.backend.application.exception.ApiException;

import java.util.UUID;

public class ShoppingListNotFoundException extends ApiException {
    public ShoppingListNotFoundException(UUID id) {
        super("Shopping list with id " + id + " not found");


    }
}
