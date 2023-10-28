package com.oxygensend.backend.integration.application.auth.jwt.factory;

import com.oxygensend.backend.application.auth.jwt.factory.TokenPayloadFactoryProvider;
import com.oxygensend.backend.domain.auth.TokenType;
import com.oxygensend.backend.domain.auth.User;
import com.oxygensend.backend.infrastructure.auth.repository.UserRepository;
import com.oxygensend.backend.integration.BaseITest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@Sql(scripts = {"classpath:data/user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:data/clear_db.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TokenPayloadFactoryProviderITest extends BaseITest {

    @Autowired
    private TokenPayloadFactoryProvider provider;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = userRepository.findAll().stream().findFirst().orElseThrow();
    }

    @Test
    void createToken_callsFactoryMethod() {
        Date exp = new Date();
        Date iat = new Date();
        TokenType type = TokenType.ACCESS;


        var token = provider.createToken(type, exp, iat, user);


    }

    @Test
    void createToken_withClaims_callsFactoryMethod() {

        Map<String, Object> map = new HashMap<>();
        map.put("firstName", "test");
        map.put("lastName", "test");
        map.put("sub", user.id().toString());
        map.put("iat", new Date());
        map.put("exp", new Date());

        Claims claims = new DefaultClaims(map);
        TokenType type = TokenType.REFRESH;


        var token = provider.createToken(type, claims);

    }


}
