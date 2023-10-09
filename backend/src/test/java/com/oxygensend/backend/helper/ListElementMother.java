package com.oxygensend.backend.helper;

import com.oxygensend.backend.domain.shooping_list.Grammar;
import com.oxygensend.backend.domain.shooping_list.ListElement;

import java.util.UUID;

public final class ListElementMother {
    private ListElementMother() {
    }

    public static ListElement getRandom() {
        return ListElement.builder()
                .id(UUID.randomUUID())
                .grammar(Grammar.LITER)
                .completed(false)
                .quantity(10)
                .product(ProductMother.getRandom())
                .build();
    }
}
