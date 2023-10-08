package com.oxygensend.backend.application.auth.jwt.factory;

import com.oxygensend.backend.application.auth.jwt.payload.AccessTokenPayload;
import com.oxygensend.backend.application.auth.jwt.payload.TokenPayload;
import com.oxygensend.backend.domain.auth.TokenType;
import com.oxygensend.backend.domain.auth.User;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AccessTokenPayloadFactory implements TokenPayloadFactory {
    @Override
    public TokenPayload createToken(Date exp, Date iat, User user) {
        return new AccessTokenPayload(
                user.firstName(),
                user.lastName(),
                user.email(),
                iat,
                exp);
    }

    @Override
    public TokenPayload createToken(Claims claims) {
        return new AccessTokenPayload(
                (String) claims.get("firstName"),
                (String) claims.get("lastName"),
                claims.getSubject(),
                claims.getIssuedAt(),
                claims.getExpiration());
    }

    @Override
    public TokenType getType() {
        return TokenType.ACCESS;
    }
}
