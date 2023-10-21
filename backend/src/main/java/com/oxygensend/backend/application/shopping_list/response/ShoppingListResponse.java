package com.oxygensend.backend.application.shopping_list.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxygensend.backend.application.shopping_list.dto.ListElementDto;
import com.oxygensend.backend.domain.shooping_list.ShoppingList;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ShoppingListResponse(
        UUID id,
        String name,
        boolean completed,
        Set<ListElementDto> products,
        @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
        LocalDateTime dateOfExecution,
        @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
        LocalDateTime updatedAt

) {

    public static ShoppingListResponse fromEntity(ShoppingList shoppingList) {
        var products = shoppingList.listElements().stream().map(ListElementDto::fromEntity).collect(Collectors.toSet());
        return new ShoppingListResponse(
                shoppingList.id(),
                shoppingList.name(),
                shoppingList.completed(),
                products,
                shoppingList.dateOfExecution(),
                shoppingList.createdAt(),
                shoppingList.updatedAt()
        );
    }
}
