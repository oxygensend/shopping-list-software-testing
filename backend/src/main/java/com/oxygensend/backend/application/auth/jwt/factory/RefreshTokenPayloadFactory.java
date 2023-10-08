package com.oxygensend.backend.application.auth.jwt.factory;

import com.oxygensend.backend.application.auth.jwt.payload.RefreshTokenPayload;
import com.oxygensend.backend.application.auth.jwt.payload.TokenPayload;
import com.oxygensend.backend.domain.auth.TokenType;
import com.oxygensend.backend.domain.auth.User;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class RefreshTokenPayloadFactory implements TokenPayloadFactory {
    @Override
    public TokenPayload createToken(Date exp, Date iat, User user) {
        return new RefreshTokenPayload(
                user.id(),
                iat,
                exp
        );
    }

    @Override
    public TokenPayload createToken(Claims claims) {
        return new RefreshTokenPayload(
                UUID.fromString(claims.getSubject()),
                claims.getIssuedAt(),
                claims.getExpiration()
        );
    }

    @Override
    public TokenType getType() {
        return TokenType.REFRESH;
    }
}
