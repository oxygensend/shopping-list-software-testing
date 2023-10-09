package com.oxygensend.backend.helper;

import com.oxygensend.backend.domain.shooping_list.Product;

import java.util.UUID;

public final class ProductMother {
    private ProductMother() {}

    public static Product getRandom() {
        var id = UUID.randomUUID();
        return Product.builder()
                .id(id)
                .name("test " + id)
                .build();
    }
}
