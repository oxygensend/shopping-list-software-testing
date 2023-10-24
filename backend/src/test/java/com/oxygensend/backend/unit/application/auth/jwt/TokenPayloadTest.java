package com.oxygensend.backend.unit.application.auth.jwt;

import com.oxygensend.backend.application.auth.jwt.payload.AccessTokenPayload;
import com.oxygensend.backend.application.auth.jwt.payload.RefreshTokenPayload;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class TokenPayloadTest {


    @Test
    public void test_AccessTokenPayloadToClaims() {
        // Arrange
        var accessTokenPayload = new AccessTokenPayload("test", "test", "test@tes.com", new Date(), new Date());

        // Act
        var claims = accessTokenPayload.toClaims();

        // Assert
        assertInstanceOf(Claims.class, accessTokenPayload.toClaims());
        assertEquals(claims.getExpiration().toString(), accessTokenPayload.exp().toString());
        assertEquals(claims.getSubject(), accessTokenPayload.email());
        assertEquals(claims.getIssuedAt().toString(), accessTokenPayload.iat().toString());
        assertEquals(claims.get("type"), accessTokenPayload.type());
        assertEquals(claims.get("lastName"), accessTokenPayload.lastName());
        assertEquals(claims.get("firstName"), accessTokenPayload.firstName());
    }

    @Test
    public void test_RefreshTokenPayloadToClaims() {
        // Arrange
        var accessTokenPayload = new RefreshTokenPayload(UUID.randomUUID(), new Date(), new Date());

        // Act
        var claims = accessTokenPayload.toClaims();

        // Assert
        assertInstanceOf(Claims.class, accessTokenPayload.toClaims());
        assertEquals(claims.getExpiration().toString(), accessTokenPayload.exp().toString());
        assertEquals(UUID.fromString(claims.getSubject()), accessTokenPayload.sessionId());
        assertEquals(claims.getIssuedAt().toString(), accessTokenPayload.iat().toString());
        assertEquals(claims.get("type"), accessTokenPayload.type());
    }
}
