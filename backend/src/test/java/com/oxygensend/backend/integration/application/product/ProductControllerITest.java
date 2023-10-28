package com.oxygensend.backend.integration.application.product;

import com.oxygensend.backend.application.product.dto.ProductsResponse;
import com.oxygensend.backend.domain.shooping_list.Grammar;
import com.oxygensend.backend.domain.shooping_list.Product;
import com.oxygensend.backend.infrastructure.shopping_list.repository.ProductRepository;
import com.oxygensend.backend.integration.BaseMvcITest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Sql(scripts = {"classpath:data/user.sql", "classpath:data/session.sql", "classpath:data/products.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:data/clear_db.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProductControllerITest extends BaseMvcITest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @WithUserDetails("test@test.com")
    public void test_GetProducts() throws Exception {

        var products = productRepository.findAll().stream().map(Product::name).collect(Collectors.toSet());
        var response = new ProductsResponse(products, Arrays.stream(Grammar.values()).collect(Collectors.toSet()));
        var serializedResponse = objectMapper.writeValueAsString(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().json(serializedResponse))
               .andDo(print());
    }

    @Test
    public void test_GetProducts_ShouldThrowUnauthorizedExceptionWhenNoAccessTokenProvided() throws Exception {

        var products = productRepository.findAll().stream().map(Product::name).collect(Collectors.toSet());
        var response = new ProductsResponse(products, Arrays.stream(Grammar.values()).collect(Collectors.toSet()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products"))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized())
               .andDo(print());
    }
}
