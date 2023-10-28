package com.oxygensend.backend.integration.application.auth.jwt;

import com.oxygensend.backend.application.auth.jwt.TokenStorage;
import com.oxygensend.backend.application.auth.jwt.factory.RefreshTokenPayloadFactory;
import com.oxygensend.backend.application.auth.jwt.payload.RefreshTokenPayload;
import com.oxygensend.backend.application.auth.jwt.payload.TokenPayload;
import com.oxygensend.backend.config.TokenConfiguration;
import com.oxygensend.backend.domain.auth.Session;
import com.oxygensend.backend.domain.auth.TokenType;
import com.oxygensend.backend.infrastructure.auth.repository.SessionRepository;
import com.oxygensend.backend.integration.BaseITest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {"classpath:data/user.sql", "classpath:data/session.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:data/clear_db.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TokenStorageITest extends BaseITest {

    @Autowired
    private TokenStorage tokenStorage;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TokenConfiguration tokenConfiguration;
    private Session session;

    @Autowired
    private RefreshTokenPayloadFactory tokenPayloadFactory;

    @BeforeEach
    public void setUp() {
        session = sessionRepository.findAll().stream().findFirst().orElseThrow();
    }

    @Test
    public void testGenerateToken() {
        // Arrange
        Date currentDate = new Date();
        Date expDate = new Date(System.currentTimeMillis() + 3600);
        var session = sessionRepository.findAll().stream().findFirst().orElseThrow();
        var payload = new RefreshTokenPayload(session.id(), currentDate, expDate);


        // Act
        String token = tokenStorage.generateToken(payload);

        // Assert
        assertNotNull(token);
    }


    @Test
    public void testValidate_ValidTokenAndType() {
        // Arrange

        TokenType type = TokenType.REFRESH;
        Claims claims = Jwts.claims()
                            .subject(session.id().toString())
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
                           .signWith(tokenConfiguration.getSignInKey())
                           .compact();
        TokenPayload expectedPayload = tokenPayloadFactory.createToken(claims);

        // Act
        TokenPayload result = tokenStorage.validate(token, type);

        // Assert
        assertEquals(expectedPayload, result);
    }


}
