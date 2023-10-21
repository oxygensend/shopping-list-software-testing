package com.oxygensend.backend.application.product.dto;

import com.oxygensend.backend.domain.shooping_list.Grammar;

import java.util.Set;

public record ProductsResponse(Set<String> names, Set<Grammar> grammarNames) {
}
