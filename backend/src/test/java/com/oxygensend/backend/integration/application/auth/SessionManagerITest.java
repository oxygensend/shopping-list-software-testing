package com.oxygensend.backend.integration.application.auth;

import com.oxygensend.backend.application.auth.SessionManager;
import com.oxygensend.backend.application.auth.response.AuthenticationResponse;
import com.oxygensend.backend.domain.auth.Session;

import com.oxygensend.backend.domain.auth.exception.SessionExpiredException;
import com.oxygensend.backend.infrastructure.auth.repository.SessionRepository;
import com.oxygensend.backend.infrastructure.auth.repository.UserRepository;
import com.oxygensend.backend.integration.BaseITest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {"classpath:data/user.sql", "classpath:data/session.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:data/clear_db.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class SessionManagerITest extends BaseITest {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    private Session session;

    @BeforeEach
    protected void setUp() {
        session = sessionRepository.findAll().stream().findFirst().orElseThrow();
    }

    @Test
    public void testStartSession_DeleteByIdAndSave() {
        sessionManager.startSession(session.id());

        var foundSession = sessionRepository.findById(session.id());
        assertEquals(session.id(), foundSession.get().id());
    }

    @Test
    public void testGetSession_Found() {
        // Arrange
        var sessionId = session.id();

        // Act
        var result = sessionManager.getSession(sessionId);

        // Assert
        assertEquals(session.id(), result.id());
    }

    @Test
    public void testGetSession_NotFound() {
        // Arrange
        UUID sessionId = UUID.randomUUID();

        // Act && assert
        assertThrows(SessionExpiredException.class, () -> sessionManager.getSession(sessionId));
    }


    @Test
    public void testPrepareSession() {
        // Arrange
        var user = userRepository.findById(session.id()).orElseThrow();

        // Act
        AuthenticationResponse response = sessionManager.prepareSession(user);

        // Assert
        var foundSession = sessionRepository.findById(session.id());
        assertEquals(session.id(), foundSession.get().id());
        assertNotNull(response.accessToken());
        assertNotNull(response.refreshToken());


    }
}
