package com.oxygensend.backend.unit.application.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oxygensend.backend.application.product.ProductController;
import com.oxygensend.backend.application.product.ProductService;
import com.oxygensend.backend.application.product.dto.ProductsResponse;
import com.oxygensend.backend.domain.shooping_list.Grammar;
import com.oxygensend.backend.infrastructure.exception.ApiExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController controller;

    @Mock
    private ProductService productService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .setControllerAdvice(new ApiExceptionHandler())
                                 .build();
    }

    @Test
    public void test_GetProducts() throws Exception {

        // Arrange
        var products = Set.of("product1", "product2");
        var response = new ProductsResponse(products, Arrays.stream(Grammar.values()).collect(Collectors.toSet()));
        var serializedResponse = objectMapper.writeValueAsString(response);
        when(productService.getProducts()).thenReturn(response);

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().json(serializedResponse))
               .andDo(print());
    }
}
