package com.oxygensend.backend.application.auth.jwt.factory;

import com.oxygensend.backend.application.auth.jwt.payload.TokenPayload;
import com.oxygensend.backend.domain.auth.TokenType;
import com.oxygensend.backend.domain.auth.User;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Component
public class TokenPayloadFactoryProvider {

    private final Map<TokenType, TokenPayloadFactory> factoryMap;

    public TokenPayloadFactoryProvider(List<TokenPayloadFactory> factories) {
        this.factoryMap = factories.stream().collect(Collectors.toMap(TokenPayloadFactory::getType, factory -> factory, detectDuplicatedImplementations()));


    }

    public TokenPayload createToken(TokenType type, Date exp, Date iat, User user) {
        return getFactory(type).createToken(exp, iat, user);
    }

    public TokenPayload createToken(TokenType type, Claims claims) {
        return getFactory(type).createToken(claims);
    }

    private TokenPayloadFactory getFactory(TokenType type) {
        return Optional.ofNullable(factoryMap.get(type)).orElseThrow(() -> new RuntimeException("No factory found for token type: " + type));
    }

    private BinaryOperator<TokenPayloadFactory> detectDuplicatedImplementations() {
        return (l, r) -> {
            throw new RuntimeException("Found duplicated strategies assigned to one token type value: " + l.getClass() + " " + r.getClass());
        };
    }
}
