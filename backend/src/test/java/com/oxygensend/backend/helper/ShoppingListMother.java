package com.oxygensend.backend.helper;

import com.oxygensend.backend.domain.shooping_list.ListElement;
import com.oxygensend.backend.domain.shooping_list.ShoppingList;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

public class ShoppingListMother {
    private ShoppingListMother() {
    }

    public static ShoppingList getRandom() {
        var id = UUID.randomUUID();
        var elements = new HashSet<ListElement>();
        elements.add(ListElementMother.getRandom());
        elements.add(ListElementMother.getRandom());
        return ShoppingList.builder()
                .id(id)
                .name("Test " + id)
                .completed(false)
                .dateOfExecution(LocalDateTime.now())
                .imageAttachmentFilename("test.jpg")
                .listElements(elements)
                .build();

    }

}
