package com.oxygensend.backend.integration.application.auth;

import com.oxygensend.backend.application.auth.AuthenticationFacadeImpl;
import com.oxygensend.backend.domain.auth.User;
import com.oxygensend.backend.integration.BaseITest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
@Sql(scripts = {"classpath:data/user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:data/clear_db.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthenticationFacadeImplITest extends BaseITest {


    @Autowired
    private AuthenticationFacadeImpl authenticationFacade;

    @Test
    @WithUserDetails("test@test.com")
    public void testGetAuthenticationPrinciple() {

        // Act
        User result = authenticationFacade.getAuthenticationPrinciple();

        // Assert
        assertEquals("test@test.com", result.getUsername());
    }
}
