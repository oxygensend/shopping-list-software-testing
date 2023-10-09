package com.oxygensend.backend.infrastructure.shopping_list.repository;

import com.oxygensend.backend.domain.shooping_list.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, UUID>, com.oxygensend.backend.domain.shooping_list.ShoppingListRepository {
}
