package com.oxygensend.backend.helper;

import com.oxygensend.backend.domain.auth.User;

import java.util.UUID;

public final class UserMother {
    private UserMother() {
    }

    public static User getRandom() {
        return User
                .builder()
                .id(UUID.randomUUID())
                .firstName("Tester")
                .lastName("Tester")
                .password("test")
                .email("test@test.com")
                .build();
    }
}
