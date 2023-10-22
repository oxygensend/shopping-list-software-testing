package com.oxygensend.backend.application.shopping_list.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.oxygensend.backend.domain.shooping_list.Grammar;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductDto(
        @NotBlank
        String name,
        Grammar grammar,
        @Min(value = 0)
        float quantity
) {
}
