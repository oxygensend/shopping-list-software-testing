package com.oxygensend.backend.application.shopping_list.dto;

import com.oxygensend.backend.domain.shooping_list.Grammar;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDto(
        @NotBlank
        String name,
        @NotNull
        Grammar grammar,
        @Min(value = 0)
        float quantity
) {
}
