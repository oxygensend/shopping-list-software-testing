package com.oxygensend.backend.application.shopping_list;

import com.oxygensend.backend.application.auth.AuthenticationFacade;
import com.oxygensend.backend.application.shopping_list.dto.ProductDto;
import com.oxygensend.backend.application.shopping_list.request.CreateShoppingListRequest;
import com.oxygensend.backend.application.shopping_list.request.UpdateShoppingListRequest;
import com.oxygensend.backend.application.shopping_list.response.PagedListResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListId;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListPagedResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListResponse;
import com.oxygensend.backend.application.storage.StorageService;
import com.oxygensend.backend.domain.auth.User;
import com.oxygensend.backend.domain.auth.exception.ShoppingListNotFoundException;
import com.oxygensend.backend.domain.auth.exception.StorageFileNotFoundException;
import com.oxygensend.backend.domain.shooping_list.ListElement;
import com.oxygensend.backend.domain.shooping_list.Product;
import com.oxygensend.backend.domain.shooping_list.ShoppingList;
import com.oxygensend.backend.infrastructure.jackson.JsonNullableWrapper;
import com.oxygensend.backend.infrastructure.shopping_list.repository.ProductRepository;
import com.oxygensend.backend.infrastructure.shopping_list.repository.ShoppingListRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingListService {


    private final AuthenticationFacade authentication;
    private final ShoppingListRepository shoppingListRepository;
    private final ProductRepository productRepository;
    private final EntityManager entityManager;
    private final StorageService storageService;


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

    @Transactional
    public ShoppingListId createShoppingList(CreateShoppingListRequest request, MultipartFile attachmentImage) {

        var products = request.products();
        var existingProducts = getExistingProducts(products);
        var shoppingList = ShoppingList.builder()
                                       .name(request.name())
                                       .dateOfExecution(request.dateOfExecution())
                                       .listElements(new HashSet<>())
                                       .user(getUser())
                                       .imageAttachmentFilename(null)
                                       .build();

        for (var productDto : products) {
            var listElement = createListElement(productDto, existingProducts);
            shoppingList.addListElement(listElement);
        }

        storeAttachmentImage(attachmentImage, shoppingList);

        entityManager.persist(shoppingList);
        entityManager.flush();

        return new ShoppingListId(shoppingList.id());
    }

    @Transactional
    public ShoppingListResponse updateShoppingList(UUID id, UpdateShoppingListRequest request) {
        var shoppingList = shoppingListRepository.findByIdAndUser(id, getUser())
                                                 .orElseThrow(() -> new ShoppingListNotFoundException(id));

        if (request.name() != null && !request.name().equals(shoppingList.name())) {
            shoppingList.name(request.name());
        }

        if (request.completed() != null && request.completed() != shoppingList.completed()) {
            shoppingList.completed(request.completed());
        }

        if (request.dateOfExecution() != null && request.dateOfExecution() != shoppingList.dateOfExecution()) {
            shoppingList.dateOfExecution(request.dateOfExecution());
        }

        if (request.products() != null) {
            var listElements = new HashSet<ListElement>();
            var existingProducts = getExistingProducts(request.products());
            for (var productDto : request.products()) {
                var listElement = createListElement(productDto, existingProducts);
                listElements.add(listElement);
            }
            shoppingList.listElements(listElements);
        }

        if (JsonNullableWrapper.isPresent(request.attachmentFile())) {
            var oldFilename = shoppingList.imageAttachmentFilename();
            storeAttachmentImage(JsonNullableWrapper.unwrap(request.attachmentFile()), shoppingList);
            storageService.delete(oldFilename);
        }

        entityManager.flush();

        return ShoppingListResponse.fromEntity(shoppingList);
    }

    public Resource loadAttachmentImage(String filename) {
        var resource = storageService.load(filename);
        if (resource == null) {
            throw new StorageFileNotFoundException("File not found: " + filename);
        }

        return resource;
    }

    private void storeAttachmentImage(MultipartFile image, ShoppingList shoppingList) {
        if (image != null) {
            var filename = storageService.store(image);
            shoppingList.imageAttachmentFilename(filename);
        }
    }

    private List<Product> getExistingProducts(List<ProductDto> products) {
        var productNames = products.stream().map(ProductDto::name).collect(Collectors.toSet());
        return productRepository.findByNames(productNames);
    }

    private ListElement createListElement(ProductDto productDto, List<Product> existingProducts) {
        var product = existingProducts.stream()
                                      .filter(p -> p.name().equals(productDto.name()))
                                      .findFirst()
                                      .orElse(new Product(productDto.name()));
        return ListElement.builder()
                          .product(product)
                          .grammar(productDto.grammar())
                          .quantity(productDto.quantity())
                          .build();
    }

    private User getUser() {
        return authentication.getAuthenticationPrinciple();
    }
}
