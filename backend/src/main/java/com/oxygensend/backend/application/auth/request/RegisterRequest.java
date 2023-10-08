package com.oxygensend.backend.application.auth.request;

import com.oxygensend.backend.domain.auth.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Size(min = 2)
        @Size(max = 64)
        @NotBlank
        String firstName,
        @Size(min = 2)
        @Size(max = 64)
        @NotBlank
        String lastName,
        @Size(min = 2)
        @Size(max = 64)
        @Email
        @NotBlank
        String email,
        @Size(min = 2)
        @Size(max = 200)
        @NotBlank
        String password
) {
    public User toUserEntity() {
        return User.builder()
                .email(this.email())
                .firstName(this.firstName())
                .lastName(this.lastName())
                .build();
    }
}
