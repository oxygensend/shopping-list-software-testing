package com.oxygensend.backend.application.shopping_list.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.oxygensend.backend.application.shopping_list.dto.ProductDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;


public record CreateShoppingListRequest(

        @NotBlank
        String name,
        @NotEmpty
        @Valid
        List<ProductDto> products,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime dateOfExecution

) {
}
