package com.oxygensend.backend.application.auth.jwt.factory;

import com.oxygensend.backend.application.auth.jwt.payload.TokenPayload;
import com.oxygensend.backend.domain.auth.TokenType;
import com.oxygensend.backend.domain.auth.User;
import io.jsonwebtoken.Claims;

import java.util.Date;

public interface TokenPayloadFactory {

    TokenPayload createToken(Date exp, Date iat, User user);
    TokenPayload createToken(Claims claims);

    TokenType getType();
}
