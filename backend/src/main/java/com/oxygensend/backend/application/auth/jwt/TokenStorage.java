package com.oxygensend.backend.application.auth.jwt;

import com.oxygensend.backend.application.auth.jwt.factory.TokenPayloadFactoryProvider;
import com.oxygensend.backend.application.auth.jwt.payload.ClaimsPayload;
import com.oxygensend.backend.application.auth.jwt.payload.TokenPayload;
import com.oxygensend.backend.config.TokenConfiguration;
import com.oxygensend.backend.domain.auth.TokenType;
import com.oxygensend.backend.domain.auth.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenStorage {

    private final TokenConfiguration tokenUtils;
    private final TokenPayloadFactoryProvider tokenPayloadFactory;

    public String generateToken(ClaimsPayload payload) {

        return Jwts.builder()
                .claims(payload.toClaims())
                .signWith(tokenUtils.getSignInKey())
                .compact();
    }


    public TokenPayload validate(String token, TokenType type) {
        Claims claims = extractClaims(token);
        TokenPayload payload = tokenPayloadFactory.createToken(type, claims);

        if (payload.type() != type) {
            throw new TokenException("Invalid token");
        }

        return payload;
    }

    private Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(tokenUtils.getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}