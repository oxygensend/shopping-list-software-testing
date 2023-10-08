package com.oxygensend.backend.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
@Component
public class TokenConfiguration {
    @Value("${jwt.secretKey}")
    public  String secretKey;
    @Value("${jwt.authExpirationMs}")
    public  int authExpirationMs;
    @Value("${jwt.refreshExpirationMs}")
    public  int refreshExpirationMs;

    public SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
