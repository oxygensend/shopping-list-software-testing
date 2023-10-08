package com.oxygensend.backend.application.auth.jwt.payload;

import io.jsonwebtoken.Claims;

public interface ClaimsPayload {
    Claims toClaims();

}
