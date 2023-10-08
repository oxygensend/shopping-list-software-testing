package com.oxygensend.backend.infrastructure.shopping_list.repository;

import com.oxygensend.backend.domain.shooping_list.ListElement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListElementRepository extends JpaRepository<ListElement, UUID>, com.oxygensend.backend.domain.shooping_list.ListElementRepository {
}
