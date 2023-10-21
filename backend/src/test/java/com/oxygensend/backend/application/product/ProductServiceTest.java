package com.oxygensend.backend.application.product;

import com.oxygensend.backend.application.product.dto.ProductsResponse;
import com.oxygensend.backend.domain.shooping_list.Grammar;
import com.oxygensend.backend.domain.shooping_list.Product;
import com.oxygensend.backend.helper.ProductMother;
import com.oxygensend.backend.infrastructure.shopping_list.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;


    @Test
    public void testGetProducts() {
        // Arrange
        var products = List.of(ProductMother.getRandom(), ProductMother.getRandom());
        when(productRepository.findAll()).thenReturn(products);
        var expectedResponse = new ProductsResponse(products.stream().map(Product::name).collect(Collectors.toSet()), Arrays.stream(Grammar.values()).collect(Collectors.toSet()));

        //Act
        var response = productService.getProducts();

        //Assert
        assertEquals(expectedResponse, response);

    }
}
