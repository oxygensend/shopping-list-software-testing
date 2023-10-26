package com.oxygensend.backend.integration.application.shopping_list;

import com.oxygensend.backend.application.shopping_list.ShoppingListService;
import com.oxygensend.backend.application.shopping_list.dto.ProductDto;
import com.oxygensend.backend.application.shopping_list.request.CreateShoppingListRequest;
import com.oxygensend.backend.application.shopping_list.request.UpdateShoppingListRequest;
import com.oxygensend.backend.application.shopping_list.response.PagedListResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListPagedResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListResponse;
import com.oxygensend.backend.domain.auth.exception.ShoppingListNotFoundException;
import com.oxygensend.backend.domain.auth.exception.StorageFileNotFoundException;
import com.oxygensend.backend.domain.shooping_list.Grammar;
import com.oxygensend.backend.domain.shooping_list.ListElement;
import com.oxygensend.backend.domain.shooping_list.Product;
import com.oxygensend.backend.domain.shooping_list.ShoppingList;
import com.oxygensend.backend.infrastructure.shopping_list.repository.ProductRepository;
import com.oxygensend.backend.infrastructure.shopping_list.repository.ShoppingListRepository;
import com.oxygensend.backend.integration.BaseITest;
import com.oxygensend.backend.integration.helper.ShoppingListHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@WithUserDetails("test@test.com")
@Sql(scripts = {"classpath:data/user.sql", "classpath:data/products.sql", "classpath:data/shopping_list.sql", "classpath:data/list_element.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:data/clear_db.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ShoppingListServiceITest extends BaseITest {

    @Autowired
    private ShoppingListService service;

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private ProductRepository productRepository;

    private List<ShoppingList> shoppingLists;

    @BeforeEach
    protected void setUp() {
        shoppingLists = shoppingListRepository.findAll();
    }

    @Test
    public void test_GetPaginatedLists() {
        // Arrange
        var shoppingLists = ShoppingListHelper.getAllShoppingListPagedResponse();
        var pageable = Pageable.ofSize(10);
        var expectedResponse = new PagedListResponse<ShoppingListPagedResponse>(shoppingLists, shoppingLists.size(), 1);

        // Act
        var response = service.getAllPaginatedList(pageable);

        //Assert
        assertEquals(expectedResponse, response);
    }


    @Test
    public void test_DeleteShoppingListSuccessfully() {
        // Arrange
        var shoppingList = shoppingLists.get(0);

        // Act
        service.deleteShoppingList(shoppingList.id());

        // Assert
        var shoppingListAfterTest = shoppingListRepository.findById(shoppingList.id());
        assertEquals(Optional.empty(), shoppingListAfterTest);

    }

    @Test
    public void test_DeleteShoppingListNotFound() {
        var id = UUID.randomUUID();
        assertThrows(ShoppingListNotFoundException.class, () -> service.deleteShoppingList(id));
    }

    @Test
    public void test_GetShoppingListShouldThrowShoppingListNotFoundException() {
        var id = UUID.randomUUID();
        assertThrows(ShoppingListNotFoundException.class, () -> service.getShoppingList(id));
    }

    @Test
    @Transactional
    public void test_GetShoppingListShouldReturnList() {
        // Arrange
        var shoppingList = shoppingLists.get(0);
        var expectedResponse = ShoppingListResponse.fromEntity(shoppingList);

        // Act
        var response = service.getShoppingList(shoppingList.id());

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    public void test_CreateShoppingList_ShouldCreateShoppingListWithAllNewProducts() {
        var products = createProductsDto();
        var request = new CreateShoppingListRequest("shopping_list", products, LocalDateTime.now());

        var response = service.createShoppingList(request, null);

        var shoppingList = shoppingListRepository.findById(response.id());
        var productCount = productRepository.count();
        assertTrue(shoppingList.isPresent());
        assertEquals(3, productCount);
    }

    @Test
    public void test_CreateShoppingList_ShouldCreateShoppingListWithAllExistingProducts() {
        // Arrange
        var products = createNewProductsDto();
        var request = new CreateShoppingListRequest("shopping_list", products, LocalDateTime.now());

        // Act
        var response = service.createShoppingList(request, null);

        // Assert
        var shoppingList = shoppingListRepository.findById(response.id());
        var productCount = productRepository.count();
        assertTrue(shoppingList.isPresent());
        assertEquals(6, productCount);
    }

    @Test
    public void test_UpdateShoppingList_ShoppingListNotFound() {

        assertThrows(ShoppingListNotFoundException.class, () -> service.updateShoppingList(UUID.randomUUID(), null, null));
    }

    @Test
    public void test_UpdateShoppingList_ValidNameChange() {
        // Arrange
        var shoppingList = shoppingLists.get(0);
        var request = new UpdateShoppingListRequest("new_name", new ArrayList<>(), null, null);
        var response = service.updateShoppingList(shoppingList.id(), request, null);

        // Act
        var shoppingListAfterTest = shoppingListRepository.findById(response.id()).get();

        // Assert
        assertEquals(request.name(), shoppingListAfterTest.name());

    }

    @Test
    public void test_UpdateShoppingList_ValidExecutionDateChange() {
        // Arrange
        var shoppingList = shoppingLists.get(0);
        var newDate = LocalDateTime.of(2024, 2, 2, 1, 1, 1);
        var request = new UpdateShoppingListRequest(null, new ArrayList<>(), newDate, null);

        // Act
        var response = service.updateShoppingList(shoppingList.id(), request, null);

        // Assert
        var shoppingListAfterTest = shoppingListRepository.findById(response.id()).get();

        assertEquals(request.dateOfExecution().toLocalDate(), shoppingListAfterTest.dateOfExecution().toLocalDate());
    }

    @Test
    public void test_UpdateShoppingList_ValidCompletedChange() {
        // Arrange
        var shoppingList = shoppingLists.get(0);
        var request = new UpdateShoppingListRequest(null, new ArrayList<>(), null, true);

        // Act
        var response = service.updateShoppingList(shoppingList.id(), request, null);

        // Assert
        var shoppingListAfterTest = shoppingListRepository.findById(response.id()).get();

        assertEquals(request.completed(), shoppingListAfterTest.completed());
    }

    @Test
    @Transactional
    void test_UpdateShoppingList_ValidProductsChange() {
        // Arrange
        var shoppingList = shoppingLists.get(0);
        var products = createNewProductsDto();
        var request = new UpdateShoppingListRequest(null, products, null, null);

        // Act
        var response = service.updateShoppingList(shoppingList.id(), request, null);
        var returnedProducts = response.products().stream().map(l -> new ProductDto(l.product(), l.grammar(), l.quantity())).toList();

        // Assert
        var shoppingListAfterTest = shoppingListRepository.findById(response.id()).get();
        var productCount = productRepository.count();

        assertEquals(6, productCount);
        assertEquals(products.size(), returnedProducts.size());
        assertTrue(products.containsAll(returnedProducts));
        assertTrue(returnedProducts.containsAll(products));
        assertTrue(shoppingListAfterTest.listElements().stream()
                                        .map(ListElement::product)
                                        .map(Product::name)
                                        .collect(Collectors.toSet())
                                        .containsAll(returnedProducts.stream()
                                                                     .map(ProductDto::name)
                                                                     .collect(Collectors.toSet()))
        );
    }

//    @Test
//    void test_UpdateShoppingList_ValidImageChange() {
//        // Arrange
//        var shoppingList = ShoppingListMother.getRandom();
//        var oldFilename = shoppingList.imageAttachmentFilename();
//        var mockFile = mock(MultipartFile.class);
//        var request = new UpdateShoppingListRequest(null, new ArrayList<>(), null, null);
//        when(authentication.getAuthenticationPrinciple()).thenReturn(user);
//        when(shoppingListRepository.findByIdAndUser(shoppingList.id(), user)).thenReturn(Optional.of(shoppingList));
//        when(storageService.store(mockFile)).thenReturn("new_test.jpeg");
//
//        // Act
//        var response = service.updateShoppingList(shoppingList.id(), request, mockFile);
//
//        // Assert
//        verify(storageService, times(1)).store(mockFile);
//        verify(storageService, times(1)).delete(oldFilename);
//        assertEquals("new_test.jpeg", shoppingList.imageAttachmentFilename());
//        assertInstanceOf(ShoppingListResponse.class, response);
//    }

    @Test
    void test_LoadImageAttachment_throwExceptionFileNotFound() {
        assertThrows(StorageFileNotFoundException.class, () -> service.loadAttachmentImage("test.jpeg"));

    }

//    @Test
//    void test_LoadImageAttachment_returnResource() {
//        // Arrange
//        var filename = "test.jpeg";
//        var resource = mock(org.springframework.core.io.Resource.class);
//        when(storageService.load(filename)).thenReturn(resource);
//
//        // Act
//        var response = service.loadAttachmentImage(filename);
//
//        // Assert
//        assertEquals(resource, response);
//    }

    private List<ProductDto> createProductsDto() {
        var products = new ArrayList<ProductDto>();
        products.add(new ProductDto("Pepper", Grammar.L, 1));
        products.add(new ProductDto("Onion", Grammar.KG, 1));
        products.add(new ProductDto("Chips", Grammar.PIECE, 1));
        return products;
    }

    private List<ProductDto> createNewProductsDto() {
        var products = new ArrayList<ProductDto>();
        products.add(new ProductDto("Graphes", Grammar.KG, 1));
        products.add(new ProductDto("Pineapple", Grammar.KG, 1));
        products.add(new ProductDto("Pierogies", Grammar.PIECE, 1));
        return products;
    }

}

