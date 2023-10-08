package com.oxygensend.backend.application.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(@Email @NotBlank String email, @NotBlank String password) {
}
