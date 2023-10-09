package com.oxygensend.backend.domain.shooping_list;

import com.oxygensend.backend.domain.auth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingListRepository {

    Optional<ShoppingList> findByIdAndUser(UUID id, User user);

    Page<ShoppingList> findAllByUser(Pageable pageable, User user);
}
