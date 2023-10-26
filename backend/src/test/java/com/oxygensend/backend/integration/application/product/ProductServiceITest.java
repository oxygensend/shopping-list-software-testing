package com.oxygensend.backend.integration.application.product;

import com.oxygensend.backend.application.product.ProductService;
import com.oxygensend.backend.application.product.dto.ProductsResponse;
import com.oxygensend.backend.domain.shooping_list.Grammar;
import com.oxygensend.backend.integration.BaseITest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql(scripts = "classpath:data/products.sql")
public class ProductServiceITest extends BaseITest {


    @Autowired
    private ProductService productService;


    private Set<String> testProducts() {
        return Set.of("Chips", "Onion", "Pepper");
    }

    @Test
    public void testGetProducts() {
        // Arrange
        var expectedResponse = new ProductsResponse(testProducts(), Arrays.stream(Grammar.values()).collect(Collectors.toSet()));

        //Act
        var response = productService.getProducts();

        //Assert
        assertEquals(expectedResponse, response);

    }

}