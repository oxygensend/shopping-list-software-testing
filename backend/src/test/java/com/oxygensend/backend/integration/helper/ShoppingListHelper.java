package com.oxygensend.backend.integration.helper;

import com.oxygensend.backend.application.shopping_list.response.ShoppingListPagedResponse;

import java.util.List;
import java.util.UUID;

public class ShoppingListHelper {

    private ShoppingListHelper() {
    }

    public static List<ShoppingListPagedResponse> getAllShoppingListPagedResponse() {
        return List.of(
                new ShoppingListPagedResponse(UUID.fromString("96d7d1cf-1854-4fa7-9234-6936d1c29a36"), "Monday shopping list", false),
                new ShoppingListPagedResponse(UUID.fromString("cef182c2-dab3-4bc8-a3ff-8f87a3e2f7d9"), "Alex shopping list", true),
                new ShoppingListPagedResponse(UUID.fromString("83fd8b08-2567-4bdd-8e57-26af8c277b1e"), "Boxing shopping list", false)
        );
    }
}
