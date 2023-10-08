package com.oxygensend.backend.application.auth.jwt;

import com.oxygensend.backend.application.auth.jwt.factory.TokenPayloadFactoryProvider;
import com.oxygensend.backend.application.auth.jwt.payload.RefreshTokenPayload;
import com.oxygensend.backend.application.auth.jwt.payload.TokenPayload;
import com.oxygensend.backend.config.TokenConfiguration;
import com.oxygensend.backend.domain.auth.TokenType;
import com.oxygensend.backend.domain.auth.exception.TokenException;
import com.oxygensend.backend.helper.TokenHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenStorageTest {

    @InjectMocks
    private TokenStorage tokenStorage;
    @Mock
    private TokenPayloadFactoryProvider tokenPayloadFactory;

    @Mock
    private TokenConfiguration tokenUtils;


    @Test
    public void testGenerateToken() {
        // Arrange
        Date currentDate = new Date();
        Date expDate = new Date(System.currentTimeMillis() + 3600);
        var payload = new RefreshTokenPayload(UUID.randomUUID(), currentDate, expDate);

        when(tokenUtils.getSignInKey()).thenReturn(TokenHelper.createSigningKey());

        // Act
        String token = tokenStorage.generateToken(payload);

        // Assert
        assertNotNull(token);
        verify(tokenUtils, times(1)).getSignInKey();
    }

    @Test
    public void testValidate_ValidTokenAndType() {
        // Arrange

        TokenType type = TokenType.REFRESH;
        Claims claims = Jwts.claims()
                .subject(UUID.randomUUID().toString())
                .expiration(new Date(System.currentTimeMillis() + 3600))
                .issuedAt(new Date())
                .add("type", type)
                .build();


        String token = Jwts.builder()
                .subject(claims.getSubject())
                .expiration(claims.getExpiration())
                .issuedAt(claims.getIssuedAt())
                .claims()
                .add("type", type)
                .and()
                .signWith(TokenHelper.createSigningKey())
                .compact();
        TokenPayload expectedPayload = tokenPayloadFactory.createToken(type, claims);

        when(tokenUtils.getSignInKey()).thenReturn(TokenHelper.createSigningKey());
        when(tokenPayloadFactory.createToken(type, claims)).thenReturn(expectedPayload);


        // Act
        TokenPayload result = tokenStorage.validate(token, type);

        // Assert
        assertEquals(expectedPayload, result);
    }

    @Test
    public void testValidate_InvalidToken() {
        // Arrange
        TokenType type = TokenType.ACCESS;
        Claims claims = createClaims(type);
        when(tokenUtils.getSignInKey()).thenReturn(TokenHelper.createSigningKey());

        String token = Jwts.builder()
                .claims(claims)
                .signWith(tokenUtils.getSignInKey())
                .compact();


        RefreshTokenPayload payload = new RefreshTokenPayload(UUID.fromString(claims.getSubject()), claims.getIssuedAt(), claims.getExpiration());

        when(tokenPayloadFactory.createToken(TokenType.REFRESH, claims)).thenReturn(payload);

        // Act & Assert
        assertThrows(TokenException.class, () -> tokenStorage.validate(token, TokenType.REFRESH));

    }

    private Claims createClaims(TokenType type) {
        Date currentDate = new Date();
        Date expDate = new Date(System.currentTimeMillis() + 3600);

        return Jwts.claims()
                .subject(UUID.randomUUID().toString())
                .expiration(expDate)
                .issuedAt(currentDate)
                .add("type", type)
                .build();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TokenConfiguration tokenConfiguration() {
            TokenConfiguration config = new TokenConfiguration();
            config.secretKey = "mock_secret_key";
            config.authExpirationMs = 3600000;
            config.refreshExpirationMs = 86400000;
            return config;
        }
    }
}
