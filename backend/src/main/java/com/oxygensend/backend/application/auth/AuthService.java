package com.oxygensend.backend.application.auth;

import com.oxygensend.backend.application.auth.request.AuthenticationRequest;
import com.oxygensend.backend.application.auth.response.AuthenticationResponse;
import com.oxygensend.backend.application.auth.request.RefreshTokenRequest;
import com.oxygensend.backend.application.auth.request.RegisterRequest;
import com.oxygensend.backend.application.auth.jwt.TokenStorage;
import com.oxygensend.backend.application.auth.jwt.payload.RefreshTokenPayload;
import com.oxygensend.backend.domain.auth.TokenType;
import com.oxygensend.backend.domain.auth.User;
import com.oxygensend.backend.domain.auth.exception.SessionExpiredException;
import com.oxygensend.backend.domain.auth.exception.TokenException;
import com.oxygensend.backend.domain.auth.exception.UnauthorizedException;
import com.oxygensend.backend.domain.auth.exception.UserAlreadyExistsException;
import com.oxygensend.backend.infrastructure.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final SessionManager sessionManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenStorage tokenStorage;

    public AuthenticationResponse register(RegisterRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            throw new UserAlreadyExistsException();
        });

        var user = request.toUserEntity();
        user.password(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        return sessionManager.prepareSession(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
            return sessionManager.prepareSession((User) authentication.getPrincipal());
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {

        var payload = getRefreshTokenPayload(request.token());
        var session = sessionManager.getSession(payload.sessionId());
        var user = userRepository.findById(session.id())
                                 .orElseThrow(() -> new SessionExpiredException("User not found by session id"));

        return sessionManager.prepareSession(user);
    }

    private RefreshTokenPayload getRefreshTokenPayload(String token) {
        var payload = (RefreshTokenPayload) tokenStorage.validate(token, TokenType.REFRESH);
        if (payload.exp().before(new Date())) {
            throw new TokenException("Token expired");
        }
        return payload;
    }

}
