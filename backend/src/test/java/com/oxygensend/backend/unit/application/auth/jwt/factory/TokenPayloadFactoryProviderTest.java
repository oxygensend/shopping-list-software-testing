package com.oxygensend.backend.unit.application.auth.jwt.factory;

import com.oxygensend.backend.application.auth.jwt.factory.AccessTokenPayloadFactory;
import com.oxygensend.backend.application.auth.jwt.factory.RefreshTokenPayloadFactory;
import com.oxygensend.backend.application.auth.jwt.factory.TokenPayloadFactory;
import com.oxygensend.backend.application.auth.jwt.factory.TokenPayloadFactoryProvider;
import com.oxygensend.backend.domain.auth.TokenType;
import com.oxygensend.backend.domain.auth.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TokenPayloadFactoryProviderTest {

    private TokenPayloadFactoryProvider provider;

    @Mock
    private RefreshTokenPayloadFactory refreshTokenPayloadFactory;
    @Mock
    private AccessTokenPayloadFactory accessTokenPayloadFactory;


    @BeforeEach
    void setUp() {

        when(refreshTokenPayloadFactory.getType()).thenReturn(TokenType.REFRESH);
        when(accessTokenPayloadFactory.getType()).thenReturn(TokenType.ACCESS);
        this.provider = new TokenPayloadFactoryProvider(List.of(refreshTokenPayloadFactory, accessTokenPayloadFactory));
    }

    @Test
    void createToken_callsFactoryMethod() {
        Date exp = new Date();
        Date iat = new Date();
        User user = new User();
        TokenType type = TokenType.ACCESS;


        provider.createToken(type, exp, iat, user);

        verify(accessTokenPayloadFactory, times(1)).createToken(exp, iat, user);
        verify(refreshTokenPayloadFactory, never()).createToken(exp, iat, user);
    }

    @Test
    void createToken_withClaims_callsFactoryMethod() {
        Claims claims = mock(Claims.class);
        TokenType type = TokenType.REFRESH;


        provider.createToken(type, claims);

        verify(refreshTokenPayloadFactory, times(1)).createToken(claims);
        verify(accessTokenPayloadFactory, never()).createToken(claims);
    }


    @Test
    void constructor_withDuplicateFactories_throwsException() {
        List<TokenPayloadFactory> duplicateFactories = Arrays.asList(accessTokenPayloadFactory, accessTokenPayloadFactory);

        assertThrows(RuntimeException.class, () -> new TokenPayloadFactoryProvider(duplicateFactories));
    }

    @Test
    void testCreateTokenTokenNotSpecified() {
        Claims claims = mock(Claims.class);
        assertThrows(RuntimeException.class, () -> provider.createToken(TokenType.NOT_SPECIFIED, claims));

    }
}
