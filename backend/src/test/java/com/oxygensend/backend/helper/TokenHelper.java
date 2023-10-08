package com.oxygensend.backend.helper;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Arrays;

public class TokenHelper {

    public static SecretKey createSigningKey() {
        byte[] signingKeyBytes = new byte[64];
        Arrays.fill(signingKeyBytes, (byte) 0);
        return Keys.hmacShaKeyFor(signingKeyBytes);
    }
}
