package com.oxygensend.backend.application.shopping_list.request;

import com.oxygensend.backend.application.shopping_list.dto.ProductDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


public record CreateShoppingListRequest(

        @NotBlank
        String name,
        MultipartFile attachmentImage,
        @NotEmpty
        @Valid
        List<ProductDto> products,

        LocalDateTime dateOfExecution

) {
}
