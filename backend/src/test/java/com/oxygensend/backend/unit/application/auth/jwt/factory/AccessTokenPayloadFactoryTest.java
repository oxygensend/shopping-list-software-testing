package com.oxygensend.backend.unit.application.auth.jwt.factory;

import com.oxygensend.backend.application.auth.jwt.factory.AccessTokenPayloadFactory;
import com.oxygensend.backend.application.auth.jwt.payload.AccessTokenPayload;
import com.oxygensend.backend.application.auth.jwt.payload.TokenPayload;
import com.oxygensend.backend.domain.auth.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccessTokenPayloadFactoryTest {


    @InjectMocks
    private AccessTokenPayloadFactory factory;

    @Test
    public void testCreateTokenPayload_ValidArguments() {
        // Arrange
        Date expDate = new Date(System.currentTimeMillis() + 3600);
        Date iatDate = new Date();
        var user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("test@test.pl")
                .build();

        // Act
        TokenPayload tokenPayload = factory.createToken(expDate, iatDate, user);

        // Assert
        assertNotNull(tokenPayload);
        assertTrue(tokenPayload instanceof AccessTokenPayload);

        AccessTokenPayload accessTokenPayload = (AccessTokenPayload) tokenPayload;
        assertEquals(iatDate, accessTokenPayload.iat());
        assertEquals(expDate, accessTokenPayload.exp());
        assertEquals(user.firstName(), accessTokenPayload.firstName());
        assertEquals(user.lastName(), accessTokenPayload.lastName());
        assertEquals(user.email(), accessTokenPayload.email());
    }

    @Test
    public void testCreateTokenPayloadFromClaims_ValidClaims() {
        // Arrange
        Date expDate = new Date(System.currentTimeMillis() + 3600);
        Date iatDate = new Date();

        Claims claims = createClaims(iatDate, expDate, "John", "Doe", "john.doe@example.com");

        // Act
        TokenPayload tokenPayload = factory.createToken(claims);

        // Assert
        assertNotNull(tokenPayload);
        assertTrue(tokenPayload instanceof AccessTokenPayload);

        AccessTokenPayload accessTokenPayload = (AccessTokenPayload) tokenPayload;
        assertEquals(iatDate, accessTokenPayload.iat());
        assertEquals(expDate, accessTokenPayload.exp());
        assertEquals("John", accessTokenPayload.firstName());
        assertEquals("Doe", accessTokenPayload.lastName());
        assertEquals("john.doe@example.com", accessTokenPayload.email());
    }

    private Claims createClaims(Date iat, Date exp, String firstName, String lastName, String email) {

        Claims claims = mock(Claims.class);
        when(claims.get("firstName")).thenReturn(firstName);
        when(claims.get("lastName")).thenReturn(lastName);
        when(claims.getIssuedAt()).thenReturn(iat);
        when(claims.getExpiration()).thenReturn(exp);
        when(claims.getSubject()).thenReturn(email);

        return claims;
    }
}
