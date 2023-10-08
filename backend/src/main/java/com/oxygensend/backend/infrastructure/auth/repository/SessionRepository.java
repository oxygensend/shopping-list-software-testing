package com.oxygensend.backend.infrastructure.auth.repository;

import com.oxygensend.backend.domain.auth.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID>, com.oxygensend.backend.domain.auth.SessionRepository {
}
