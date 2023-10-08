package com.oxygensend.backend.domain.auth;

import jakarta.persistence.Access;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Getter
@Accessors(fluent = true, chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    private UUID id;

}
