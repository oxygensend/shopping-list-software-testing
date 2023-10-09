package com.oxygensend.backend.application.shopping_list.dto;

import com.oxygensend.backend.domain.shooping_list.Grammar;
import com.oxygensend.backend.domain.shooping_list.ListElement;

import java.util.UUID;

public record ListElementDto(
        UUID id,
        String product,
        Grammar grammar,
        int quantity,
        boolean completed
) {
    public static ListElementDto fromEntity(ListElement listElement) {
        return new ListElementDto(
                listElement.id(),
                listElement.product().name(),
                listElement.grammar(),
                listElement.quantity(),
                listElement.completed()
        );
    }
}
