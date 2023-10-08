package com.oxygensend.backend.infrastructure.auth.repository;

import com.oxygensend.backend.domain.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, com.oxygensend.backend.domain.auth.UserRepository {
}
