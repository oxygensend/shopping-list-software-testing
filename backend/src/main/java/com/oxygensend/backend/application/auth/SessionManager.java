package com.oxygensend.backend.application.auth;

import com.oxygensend.backend.application.auth.response.AuthenticationResponse;
import com.oxygensend.backend.application.auth.jwt.TokenStorage;
import com.oxygensend.backend.application.auth.jwt.factory.TokenPayloadFactoryProvider;
import com.oxygensend.backend.config.TokenConfiguration;
import com.oxygensend.backend.domain.auth.Session;
import com.oxygensend.backend.domain.auth.TokenType;
import com.oxygensend.backend.domain.auth.User;
import com.oxygensend.backend.domain.auth.exception.SessionExpiredException;
import com.oxygensend.backend.infrastructure.auth.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SessionManager {

    private final SessionRepository sessionRepository;
    private final TokenPayloadFactoryProvider tokenPayloadFactory;
    private final TokenConfiguration tokenConfiguration;
    private final TokenStorage tokenStorage;

    public void startSession(UUID sessionId) {
        sessionRepository.deleteById(sessionId);
        sessionRepository.save(new Session(sessionId));
    }

    public Session getSession(UUID sessionId) {
        return sessionRepository.findById(sessionId).orElseThrow(SessionExpiredException::new);
    }

    public AuthenticationResponse prepareSession(User user) {

        // Generate refresh token
        var refreshPayload = tokenPayloadFactory.createToken(
                TokenType.REFRESH,
                new Date(System.currentTimeMillis() + tokenConfiguration.authExpirationMs),
                new Date(System.currentTimeMillis()),
                user
        );
        String refreshToken = tokenStorage.generateToken(refreshPayload);

        // Start session for this user
        startSession(user.id());

        // Generate access token
        var accessPayload = tokenPayloadFactory.createToken(
                TokenType.ACCESS,
                new Date(System.currentTimeMillis() + tokenConfiguration.authExpirationMs),
                new Date(System.currentTimeMillis()),
                user
        );

        String accessToken = tokenStorage.generateToken(accessPayload);
        return new AuthenticationResponse(accessToken, refreshToken);

    }
}
