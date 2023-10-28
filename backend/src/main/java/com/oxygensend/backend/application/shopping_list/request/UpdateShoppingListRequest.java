package com.oxygensend.backend.application.shopping_list.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.oxygensend.backend.application.shopping_list.dto.ProductDto;
import java.time.LocalDateTime;
import java.util.List;

public record UpdateShoppingListRequest(
        String name,
        List<ProductDto> products,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime dateOfExecution,
        Boolean completed
) {

}
