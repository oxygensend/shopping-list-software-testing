package com.oxygensend.backend.application.shopping_list.request;

import com.oxygensend.backend.application.shopping_list.dto.ProductDto;
import java.time.LocalDateTime;
import java.util.List;

public record UpdateShoppingListRequest(
        String name,
        List<ProductDto> products,
        LocalDateTime dateOfExecution,
        Boolean completed
) {

}
