package com.oxygensend.backend.application.shopping_list;

import com.oxygensend.backend.application.auth.AuthenticationFacade;
import com.oxygensend.backend.application.shopping_list.dto.ProductDto;
import com.oxygensend.backend.application.shopping_list.request.CreateShoppingListRequest;
import com.oxygensend.backend.application.shopping_list.request.UpdateShoppingListRequest;
import com.oxygensend.backend.application.shopping_list.response.PagedListResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListId;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListPagedResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListResponse;
import com.oxygensend.backend.infrastructure.storage.FileStorageService;
import com.oxygensend.backend.domain.auth.User;
import com.oxygensend.backend.domain.auth.exception.ShoppingListNotFoundException;
import com.oxygensend.backend.domain.shooping_list.Grammar;
import com.oxygensend.backend.domain.shooping_list.Product;
import com.oxygensend.backend.domain.shooping_list.ShoppingList;
import com.oxygensend.backend.helper.ShoppingListMother;
import com.oxygensend.backend.helper.UserMother;
import com.oxygensend.backend.infrastructure.shopping_list.repository.ProductRepository;
import com.oxygensend.backend.infrastructure.shopping_list.repository.ShoppingListRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
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

    @Mock
    private FileStorageService storageService;
    @Mock
    private EntityManager entityManager;


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

    @Test
    public void test_CreateShoppingList_ShouldCreateShoppingListWithAllNewProducts() {
        // Arrange
        var file = mock(MultipartFile.class);
        var products = createProductsDto();
        var productNames = products.stream().map(ProductDto::name).collect(Collectors.toSet());
        var request = new CreateShoppingListRequest("shopping_list",  products, LocalDateTime.now());

        when(productRepository.findByNames(productNames)).thenReturn(List.of());
        when(storageService.store(file)).thenReturn("test.jpeg");


        // Act
        var response = service.createShoppingList(request, file);

        // Assert
        verify(storageService, times(1)).store(file);
        verify(productRepository, times(1)).findByNames(productNames);
        verify(entityManager, times(1)).flush();
        assertInstanceOf(ShoppingListId.class, response);
    }


    @Test
    public void test_CreateShoppingList_ShouldCreateShoppingListWithAllExistingProducts() {
        // Arrange
        var file = mock(MultipartFile.class);
        var products = createProductsDto();
        var productNames = products.stream().map(ProductDto::name).collect(Collectors.toSet());
        var request = new CreateShoppingListRequest("shopping_list",  products, LocalDateTime.now());
        var productEntities = products.stream().map(Product::productDto).toList();

        when(productRepository.findByNames(productNames)).thenReturn(productEntities);
        when(storageService.store(file)).thenReturn("test.jpeg");

        // Act
        var response = service.createShoppingList(request, file);

        // Assert
        verify(storageService, times(1)).store(file);
        verify(productRepository, times(1)).findByNames(productNames);
        verify(entityManager, times(1)).flush();
        assertInstanceOf(ShoppingListId.class, response);
    }

    @Test
    public void test_UpdateShoppingList_ShoppingListNotFound() {
        // Arrange
        var shoppingList = ShoppingListMother.getRandom();
        when(shoppingListRepository.findByIdAndUser(shoppingList.id(), user)).thenThrow(new ShoppingListNotFoundException(shoppingList.id()));

        //Act && Assert
        assertThrows(ShoppingListNotFoundException.class, () -> service.updateShoppingList(shoppingList.id(), null));
        verify(shoppingListRepository, times(1)).findByIdAndUser(shoppingList.id(), user);
    }

    @Test
    public void test_UpdateShoppingList_ValidNameChange() {
        // Arrange
        var shoppingList = ShoppingListMother.getRandom();
        var request = new UpdateShoppingListRequest("new_name", JsonNullable.undefined(), new ArrayList<>(), null, null);
        when(shoppingListRepository.findByIdAndUser(shoppingList.id(), user)).thenReturn(Optional.of(shoppingList));

        // Act
        var response = service.updateShoppingList(shoppingList.id(), request);

        // Assert
        assertEquals(shoppingList.name(), "new_name");
        assertInstanceOf(ShoppingListResponse.class, response);
    }

    @Test
    public void test_UpdateShoppingList_ValidExecutionDateChange() {
        // Arrange
        var shoppingList = ShoppingListMother.getRandom();
        var newDate = LocalDateTime.of(2024, 2, 2, 1, 1, 1);
        var request = new UpdateShoppingListRequest(null, JsonNullable.undefined(), new ArrayList<>(), newDate, null);
        when(shoppingListRepository.findByIdAndUser(shoppingList.id(), user)).thenReturn(Optional.of(shoppingList));

        // Act
        var response = service.updateShoppingList(shoppingList.id(), request);

        // Assert
        assertEquals(newDate, shoppingList.dateOfExecution());
        assertInstanceOf(ShoppingListResponse.class, response);
    }

    @Test
    public void test_UpdateShoppingList_ValidCompletedChange() {
        // Arrange
        var shoppingList = ShoppingListMother.getRandom();
        var request = new UpdateShoppingListRequest(null, JsonNullable.undefined(), new ArrayList<>(), null, true);
        when(shoppingListRepository.findByIdAndUser(shoppingList.id(), user)).thenReturn(Optional.of(shoppingList));

        // Act
        var response = service.updateShoppingList(shoppingList.id(), request);

        // Assert
        assertTrue(shoppingList.completed());
        assertInstanceOf(ShoppingListResponse.class, response);
    }

    @Test
    void test_UpdateShoppingList_ValidProductsChange() {
        // Arrange
        var shoppingList = ShoppingListMother.getRandom();
        var products = createProductsDto();
        var request = new UpdateShoppingListRequest(null, JsonNullable.undefined(), products, null, null);
        when(shoppingListRepository.findByIdAndUser(shoppingList.id(), user)).thenReturn(Optional.of(shoppingList));

        // Act
        var response = service.updateShoppingList(shoppingList.id(), request);
        var returnedProducts = response.products().stream().map(l -> new ProductDto(l.product(), l.grammar(), l.quantity())).toList();

        // Assert
        assertEquals(products.size(), returnedProducts.size());
        assertTrue(products.containsAll(returnedProducts));
        assertTrue(returnedProducts.containsAll(products));
        assertInstanceOf(ShoppingListResponse.class, response);
    }

    @Test
    void test_UpdateShoppingList_ValidImageChange() {
        // Arrange
        var shoppingList = ShoppingListMother.getRandom();
        var oldFilename = shoppingList.imageAttachmentFilename();
        var mockFile = mock(MultipartFile.class);
        var request = new UpdateShoppingListRequest(null, mockFile, new ArrayList<>(), null, null);
        when(shoppingListRepository.findByIdAndUser(shoppingList.id(), user)).thenReturn(Optional.of(shoppingList));
        when(storageService.store(mockFile)).thenReturn("new_test.jpeg");

        // Act
        var response = service.updateShoppingList(shoppingList.id(), request);

        // Assert
        verify(storageService, times(1)).store(mockFile);
        verify(storageService, times(1)).delete(oldFilename);
        assertEquals("new_test.jpeg", shoppingList.imageAttachmentFilename());
        assertInstanceOf(ShoppingListResponse.class, response);
    }


    private List<ProductDto> createProductsDto() {
        var products = new ArrayList<ProductDto>();
        products.add(new ProductDto("product_1", Grammar.L, 1));
        products.add(new ProductDto("product_2", Grammar.KG, 1));
        products.add(new ProductDto("product_3", Grammar.PIECE, 1));
        return products;
    }

    private List<ShoppingList> createShoppingLists() {
        var shoppingLists = new ArrayList<ShoppingList>();
        shoppingLists.add(ShoppingListMother.getRandom());
        shoppingLists.add(ShoppingListMother.getRandom());
        shoppingLists.add(ShoppingListMother.getRandom());
        return shoppingLists;
    }


}
