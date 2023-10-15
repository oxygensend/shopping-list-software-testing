package com.oxygensend.backend.application.shopping_list;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oxygensend.backend.application.shopping_list.response.PagedListResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListPagedResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListResponse;
import com.oxygensend.backend.domain.shooping_list.ShoppingList;
import com.oxygensend.backend.helper.ShoppingListMother;
import com.oxygensend.backend.infrastructure.exception.ApiExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class ShoppingControllerTest {

    @InjectMocks
    private ShoppingListController controller;
    @Mock
    private ShoppingListService service;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setValidator(new LocalValidatorFactoryBean())
                .setControllerAdvice(new ApiExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

    }

    @Test
    public void test_Get_ShouldReturnValidResponse() throws Exception {
        // Arrange
        var response = ShoppingListResponse.fromEntity(ShoppingListMother.getRandom());
        var serializedResponse = objectMapper.writeValueAsString(response);
        var listId = response.id();

        when(service.getShoppingList(listId)).thenReturn(response);

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/shopping-lists/%s", listId))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(serializedResponse));

    }

    @Test
    public void test_Get_ShouldThrowExceptionWhenWrongParamTypeIsGiven() throws Exception {

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/shopping-lists/%s", "abcf"))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());

    }


    @Test
    public void test_Delete_ShouldReturnNoContent() throws Exception {
        // Arrange
        var listId = UUID.randomUUID();


        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/v1/shopping-lists/%s", listId))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    public void test_Delete_ShouldThrowExceptionWhenWrongParamTypeIsGiven() throws Exception {

        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/v1/shopping-lists/%s", "abcf"))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());

    }

    @Test
    public void test_GetAllPaginated_ShouldReturn200AndArrayOfShoppingLists() throws Exception {
        // Arrange
        var shoppingLists = new ArrayList<ShoppingList>();
        shoppingLists.add(ShoppingListMother.getRandom());
        shoppingLists.add(ShoppingListMother.getRandom());
        shoppingLists.add(ShoppingListMother.getRandom());

        var shoppingListDtoLists = shoppingLists.stream().map(ShoppingListPagedResponse::fromEntity).toList();
        var response = new PagedListResponse<ShoppingListPagedResponse>(shoppingListDtoLists, 1, 3);
        var serializedResponse = objectMapper.writeValueAsString(response);

        when(service.getAllPaginatedList(any(Pageable.class))).thenReturn(response);
        System.out.println(serializedResponse);
        // Act && Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/shopping-lists")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(serializedResponse))
                .andDo(print());

    }


}
