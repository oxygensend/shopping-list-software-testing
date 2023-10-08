package com.oxygensend.backend.application.auth.response;

public record AuthenticationResponse(String accessToken, String refreshToken) {
}
