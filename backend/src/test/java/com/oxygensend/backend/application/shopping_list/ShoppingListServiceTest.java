package com.oxygensend.backend.application.shopping_list;

import com.oxygensend.backend.application.auth.AuthenticationFacade;
import com.oxygensend.backend.application.shopping_list.response.PagedListResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListPagedResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListResponse;
import com.oxygensend.backend.domain.auth.User;
import com.oxygensend.backend.domain.auth.exception.ShoppingListNotFoundException;
import com.oxygensend.backend.domain.shooping_list.ShoppingList;
import com.oxygensend.backend.helper.ShoppingListMother;
import com.oxygensend.backend.helper.UserMother;
import com.oxygensend.backend.infrastructure.shopping_list.repository.ProductRepository;
import com.oxygensend.backend.infrastructure.shopping_list.repository.ShoppingListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShoppingListServiceTest {

    @InjectMocks
    private ShoppingListService service;
    @Mock
    private ShoppingListRepository shoppingListRepository;
    @Mock
    private ProductRepository productRepository;

    @Mock
    private AuthenticationFacade authentication;

    private final User user = UserMother.getRandom();


    @BeforeEach
    public void setUp() {
        when(authentication.getAuthenticationPrinciple()).thenReturn(user);
    }

    //
    @Test
    public void test_GetPaginatedLists() {
        // Arrange
        var shoppingLists = createShoppingLists();
        var pageable = Pageable.ofSize(1);
        var page = new PageImpl<ShoppingList>(shoppingLists, pageable, shoppingLists.size());
        var shoppingListDtoLists = shoppingLists.stream().map(ShoppingListPagedResponse::fromEntity).toList();
        var expectedResponse = new PagedListResponse<ShoppingListPagedResponse>(shoppingListDtoLists, page.getNumberOfElements(), page.getTotalPages());

        when(shoppingListRepository.findAllByUser(pageable, user)).thenReturn(page);

        // Act
        var response = service.getAllPaginatedList(pageable);

        //Assert
        assertEquals(expectedResponse, response);
        verify(shoppingListRepository, times(1)).findAllByUser(pageable, user);
    }

    @Test
    public void test_DeleteShoppingListSuccessfully() {
        // Arrange
        var shoppingList = ShoppingListMother.getRandom();
        when(shoppingListRepository.findByIdAndUser(shoppingList.id(), user)).thenReturn(Optional.of(shoppingList));

        // Act
        service.deleteShoppingList(shoppingList.id());

        // Assert
        verify(shoppingListRepository, times(1)).delete(shoppingList);
        verify(shoppingListRepository, times(1)).findByIdAndUser(shoppingList.id(), user);

    }

    @Test
    public void test_DeleteShoppingListNotFound() {
        // Arrange
        var id = UUID.randomUUID();
        when(shoppingListRepository.findByIdAndUser(id, user)).thenThrow(new ShoppingListNotFoundException(id));

        //Act && Assert
        assertThrows(ShoppingListNotFoundException.class, () -> service.deleteShoppingList(id));
        verify(shoppingListRepository, times(1)).findByIdAndUser(id, user);

    }

    @Test
    public void test_GetShoppingListShouldThrowShoppingListNotFoundException() {
        // Arrange
        var id = UUID.randomUUID();
        when(shoppingListRepository.findByIdAndUser(id, user)).thenThrow(new ShoppingListNotFoundException(id));

        //Act && Assert
        assertThrows(ShoppingListNotFoundException.class, () -> service.getShoppingList(id));
        verify(shoppingListRepository, times(1)).findByIdAndUser(id, user);


    }

    @Test
    public void test_GetShoppingListShouldReturnList() {
        // Arrange
        var shoppingList = ShoppingListMother.getRandom();
        var expectedResponse = ShoppingListResponse.fromEntity(shoppingList);
        when(shoppingListRepository.findByIdAndUser(shoppingList.id(), user)).thenReturn(Optional.of(shoppingList));

        // Act
        var response = service.getShoppingList(shoppingList.id());

        // Assert
        assertEquals(expectedResponse, response);
        verify(shoppingListRepository, times(1)).findByIdAndUser(shoppingList.id(), user);
    }

    private List<ShoppingList> createShoppingLists() {
        var shoppingLists = new ArrayList<ShoppingList>();
        shoppingLists.add(ShoppingListMother.getRandom());
        shoppingLists.add(ShoppingListMother.getRandom());
        shoppingLists.add(ShoppingListMother.getRandom());
        return shoppingLists;
    }


}
