package com.oxygensend.backend.application.shopping_list;

import com.oxygensend.backend.application.auth.AuthenticationFacade;
import com.oxygensend.backend.application.shopping_list.response.PagedListResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListPagedResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListResponse;
import com.oxygensend.backend.domain.auth.User;
import com.oxygensend.backend.domain.auth.exception.ShoppingListNotFoundException;
import com.oxygensend.backend.domain.shooping_list.ShoppingList;
import com.oxygensend.backend.infrastructure.shopping_list.repository.ProductRepository;
import com.oxygensend.backend.infrastructure.shopping_list.repository.ShoppingListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingListService {


    private final AuthenticationFacade authentication;
    private final ShoppingListRepository shoppingListRepository;
    private final ProductRepository productRepository;


    public PagedListResponse<ShoppingListPagedResponse> getAllPaginatedList(Pageable pageable) {
        Page<ShoppingList> paginator = shoppingListRepository.findAllByUser(pageable, getUser());
        var data = paginator.map(ShoppingListPagedResponse::fromEntity).toList();

        return new PagedListResponse<>(data, paginator.getNumberOfElements(), paginator.getTotalPages());
    }

    public void deleteShoppingList(UUID shoppingListId) {
        var shoppingList = shoppingListRepository.findByIdAndUser(shoppingListId, getUser())
                .orElseThrow(() -> new ShoppingListNotFoundException(shoppingListId));

        shoppingListRepository.delete(shoppingList);
    }

    public ShoppingListResponse getShoppingList(UUID id) {
        var shoppingList = shoppingListRepository.findByIdAndUser(id, getUser())
                .orElseThrow(() -> new ShoppingListNotFoundException(id));

        return ShoppingListResponse.fromEntity(shoppingList);
    }

    private User getUser() {
        return authentication.getAuthenticationPrinciple();
    }
}
