package com.oxygensend.backend.application.shopping_list.response;

import com.oxygensend.backend.domain.shooping_list.ShoppingList;

import java.util.UUID;

public record ShoppingListPagedResponse(UUID id, String name, boolean completed) {

    public static ShoppingListPagedResponse fromEntity(ShoppingList list) {
        return new ShoppingListPagedResponse(list.id(), list.name(), list.completed());
    }
}
