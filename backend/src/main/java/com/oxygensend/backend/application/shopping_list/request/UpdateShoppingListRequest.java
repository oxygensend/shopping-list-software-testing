package com.oxygensend.backend.application.shopping_list.request;

import com.oxygensend.backend.application.shopping_list.dto.ProductDto;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

public record UpdateShoppingListRequest(
        String name,
        JsonNullable<MultipartFile> attachmentFile,
        List<ProductDto> products,
        LocalDateTime dateOfExecution,
        Boolean completed
) {

    public UpdateShoppingListRequest(
            String name,
            MultipartFile multipartFile,
            List<ProductDto> products,
            LocalDateTime dateOfExecution,
            Boolean completed
    ) {
        this(name, JsonNullable.of(multipartFile), products, dateOfExecution, completed);
    }


}
