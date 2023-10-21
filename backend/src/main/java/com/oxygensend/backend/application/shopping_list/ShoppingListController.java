package com.oxygensend.backend.application.shopping_list;

import com.oxygensend.backend.application.shopping_list.request.CreateShoppingListRequest;
import com.oxygensend.backend.application.shopping_list.request.UpdateShoppingListRequest;
import com.oxygensend.backend.application.shopping_list.response.PagedListResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListId;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListPagedResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shopping-lists")
public class ShoppingListController {

    private final ShoppingListService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingListResponse get(@PathVariable UUID id) {
        return service.getShoppingList(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.deleteShoppingList(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedListResponse<ShoppingListPagedResponse> getAllPaginated(Pageable pageable) {
        return service.getAllPaginatedList(pageable);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingListId createShoppingList(@ModelAttribute @Validated CreateShoppingListRequest request) {
        return service.createShoppingList(request);
    }

    @PatchMapping("/{id}" )
    @ResponseStatus(HttpStatus.OK)
    public ShoppingListResponse createShoppingList(@PathVariable UUID id, @ModelAttribute @Validated UpdateShoppingListRequest request) {
        return service.updateShoppingList(id, request);
    }


}
