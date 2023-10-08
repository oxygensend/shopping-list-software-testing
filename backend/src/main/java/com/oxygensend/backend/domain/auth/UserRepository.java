package com.oxygensend.backend.domain.auth;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);
}
