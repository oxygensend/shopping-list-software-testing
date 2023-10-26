package com.oxygensend.backend.integration.application.auth;

import com.oxygensend.backend.application.auth.AuthService;
import com.oxygensend.backend.application.auth.request.AuthenticationRequest;
import com.oxygensend.backend.application.auth.request.RegisterRequest;
import com.oxygensend.backend.application.auth.response.AuthenticationResponse;
import com.oxygensend.backend.config.TokenConfiguration;
import com.oxygensend.backend.domain.auth.User;
import com.oxygensend.backend.domain.auth.exception.UserAlreadyExistsException;
import com.oxygensend.backend.infrastructure.auth.repository.SessionRepository;
import com.oxygensend.backend.infrastructure.auth.repository.UserRepository;
import com.oxygensend.backend.integration.BaseITest;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@Sql(scripts = {"classpath:data/user.sql", "classpath:data/session.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:data/clear_db.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthServiceITest extends BaseITest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenConfiguration tokenUtils;
    private User user;

    @BeforeEach
    protected void setUp() {
        user = userRepository.findAll().stream().findFirst().orElseThrow();
    }

    @Test
    public void testAuthenticate_ValidCredentials() {
        // Arrange
        String email = "test@test.com";
        String password = "test";
        AuthenticationRequest request = new AuthenticationRequest(email, password);

        // Act
        AuthenticationResponse response = authService.authenticate(request);

        // Assert
        var session = sessionRepository.findById(user.id()).orElseThrow();


        assertNotNull(response.accessToken());
        assertNotNull(response.refreshToken());
        assertEquals(user.id(), session.id());
    }

    @Test
    public void testAuthenticate_InvalidCredentials() {
        // Arrange
        String email = "test@test.com";
        String password = "wrong_password";
        AuthenticationRequest request = new AuthenticationRequest(email, password);


        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.authenticate(request));
    }

    @Test
    public void testRegister_NewUser() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        RegisterRequest request = new RegisterRequest("John", "Doe", email, password);


        // Act
        AuthenticationResponse response = authService.register(request);

        // Assert

        var newUser = userRepository.findByEmail(email).orElseThrow();
        var session = sessionRepository.findById(newUser.id()).orElseThrow();


        assertEquals(newUser.firstName(), request.firstName());
        assertEquals(newUser.lastName(), request.lastName());
        assertEquals(newUser.email(), request.email());
        assertTrue(passwordEncoder.matches(request.password(), newUser.password()));
        assertEquals(newUser.id(), session.id());

        assertNotNull(response.accessToken());
        assertNotNull(response.refreshToken());

    }


    @Test
    public void testRegister_ExistingUser() {
        // Arrange
        String email = "test@test.com";
        String password = "password";
        RegisterRequest request = new RegisterRequest("John", "Doe", email, password);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
    }


//    @Test
//    public void test_RefreshToken() {
//
//        // Arrange
//        var token = Jwts.builder()
//                        .claims(createClaims(new Date(), new Date(System.currentTimeMillis() + 1000), "test", "test", "test@test.com"))
//                        .signWith(tokenUtils.getSignInKey())
//                        .compact();
//
//        RefreshTokenRequest request = new RefreshTokenRequest(token);
//
//        // Act
//        AuthenticationResponse response = authService.refreshToken(request);
//
//        // Assert
//        assertNotNull(response.accessToken());
//        assertNotNull(response.refreshToken());
//
//    }

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
