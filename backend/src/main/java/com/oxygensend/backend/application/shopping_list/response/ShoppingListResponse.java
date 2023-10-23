package com.oxygensend.backend.application.shopping_list.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
        String imageAttachmentFilename,
        Set<ListElementDto> products,
        @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime dateOfExecution,
        @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime updatedAt

) {

    public static ShoppingListResponse fromEntity(ShoppingList shoppingList) {
        var products = shoppingList.listElements().stream().map(ListElementDto::fromEntity).collect(Collectors.toSet());
        return new ShoppingListResponse(
                shoppingList.id(),
                shoppingList.name(),
                shoppingList.completed(),
                shoppingList.imageAttachmentFilename(),
                products,
                shoppingList.dateOfExecution(),
                shoppingList.createdAt(),
                shoppingList.updatedAt()
        );
    }
}
