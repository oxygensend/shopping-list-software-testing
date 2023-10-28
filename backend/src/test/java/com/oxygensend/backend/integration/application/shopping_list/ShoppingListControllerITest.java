package com.oxygensend.backend.integration.application.shopping_list;

import com.oxygensend.backend.application.shopping_list.dto.ProductDto;
import com.oxygensend.backend.application.shopping_list.request.CreateShoppingListRequest;
import com.oxygensend.backend.application.shopping_list.request.UpdateShoppingListRequest;
import com.oxygensend.backend.application.shopping_list.response.PagedListResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListPagedResponse;
import com.oxygensend.backend.application.shopping_list.response.ShoppingListResponse;
import com.oxygensend.backend.application.storage.StorageProperties;
import com.oxygensend.backend.domain.shooping_list.Grammar;
import com.oxygensend.backend.domain.shooping_list.ShoppingList;
import com.oxygensend.backend.infrastructure.shopping_list.repository.ShoppingListRepository;
import com.oxygensend.backend.integration.BaseMvcITest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {"classpath:data/user.sql", "classpath:data/products.sql", "classpath:data/shopping_list.sql", "classpath:data/list_element.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:data/clear_db.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ShoppingListControllerITest extends BaseMvcITest {

    private List<ShoppingList> shoppingLists;

    @Autowired
    private ShoppingListRepository repository;

    @Autowired
    private StorageProperties storageProperties;

    private List<ProductDto> createProductsDto() {
        var products = new ArrayList<ProductDto>();
        products.add(new ProductDto("Pepper", Grammar.L, 1));
        products.add(new ProductDto("Onion", Grammar.KG, 1));
        products.add(new ProductDto("Chips", Grammar.PIECE, 1));
        return products;
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        shoppingLists = repository.findAll();
    }


    @Test
    @WithUserDetails("test@test.com")
    @Transactional
    public void test_Get_ShouldReturnValidResponse() throws Exception {
        // Arrange
        var shoppingList = shoppingLists.stream().findFirst().orElseThrow();
        var response = ShoppingListResponse.fromEntity(shoppingList);
        var serializedResponse = objectMapper.writeValueAsString(response);

        var listId = response.id();

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/shopping-lists/%s", listId))
                                              .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.content().json(serializedResponse));

    }

    @Test
    @WithUserDetails("test@test.com")
    public void test_Get_ShouldThrowExceptionWhenWrongParamTypeIsGiven() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/shopping-lists/%s", "abcf"))
                                              .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isBadRequest())
               .andDo(print());

    }


    @Test
    public void test_Get_ShouldThrowUnAuthorizedExceptionWhenNoAccessTokenProvided() throws Exception {

        var shoppingList = shoppingLists.stream().findFirst().orElseThrow();
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/shopping-lists/%s", shoppingList.id()))
                                              .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isUnauthorized())
               .andDo(print());

    }


    @Test
    public void test_Delete_ShouldThrowUnAuthorizedExceptionWhenNoAccessTokenProvided() throws Exception {
        var shoppingList = shoppingLists.stream().findFirst().orElseThrow();


        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/v1/shopping-lists/%s", shoppingList.id()))
                                              .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("test@test.com")
    public void test_Delete_ShouldReturnNoContent_AndDeleteShoppingList() throws Exception {
        var shoppingList = shoppingLists.stream().findFirst().orElseThrow();


        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/v1/shopping-lists/%s", shoppingList.id()))
                                              .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isNoContent())
               .andExpect(MockMvcResultMatchers.content().string(""));

        var shoppingListAfterTest = repository.findById(shoppingList.id());
        assert (shoppingListAfterTest.isEmpty());
    }

    @Test
    @WithUserDetails("test@test.com")
    public void test_Delete_ShouldThrowExceptionWhenWrongParamTypeIsGiven() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/v1/shopping-lists/%s", "abcf"))
                                              .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isBadRequest())
               .andDo(print());

    }

    @Test
    @WithUserDetails("test@test.com")
    public void test_GetAllPaginated_ShouldReturn200AndArrayOfShoppingLists() throws Exception {

        var shoppingListDtoLists = shoppingLists.stream().map(ShoppingListPagedResponse::fromEntity).toList();
        var response = new PagedListResponse<ShoppingListPagedResponse>(shoppingListDtoLists, 3, 1);
        var serializedResponse = objectMapper.writeValueAsString(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/shopping-lists")
                                              .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.content().json(serializedResponse))
               .andDo(print());

    }

    @Test
    public void test_GetAllPaginated_ShouldThrowUnAuthorizedExceptionWhenNoAccessTokenProvided() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/shopping-lists")
                                              .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpect(status().isUnauthorized())
               .andDo(print());

    }

    @Test
    public void test_CreateShoppingList_ShouldThrowUnAuthorizedExceptionWhenNoAccessTokenProvided() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shopping-lists")
                                              .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
               )
               .andExpect(status().isUnauthorized())
               .andDo(print());

    }

    @Test
    @WithUserDetails("test@test.com")
    public void test_CreateShoppingList_CreateShoppingListValid() throws Exception {


        MockMultipartFile attachmentImage = new MockMultipartFile("attachmentImage", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());
        var request = new CreateShoppingListRequest("shopping_list", createProductsDto(), LocalDateTime.now());
        var serializedRequest = objectMapper.writeValueAsString(request);
        var requestFile = new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, serializedRequest.getBytes());


        var response = mockMvc.perform(multipart("/api/v1/shopping-lists")
                                               .file(attachmentImage)
                                               .file(requestFile)
                                               .contentType(MediaType.MULTIPART_FORM_DATA))
                              .andExpect(status().isCreated())
                              .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                              .andDo(print())
                              .andReturn();


        var content = response.getResponse().getContentAsString();
        var id = objectMapper.readTree(content).get("id").asText();

        var shoppingList = repository.findById(UUID.fromString(id)).orElseThrow();
        var filePath = storageProperties.fullShoppingListLocation() + "/" + shoppingList.imageAttachmentFilename();

        assertNotNull(shoppingList);
        assertTrue(Files.exists(Paths.get(filePath)));

        FileUtils.deleteDirectory(Paths.get(storageProperties.getRootLocation()).toFile());
    }

    @Test
    @WithUserDetails("test@test.com")
    public void test_CreateShoppingList_ThrowExceptionBadRequest_NameIsRequired() throws Exception {

        var request = new CreateShoppingListRequest(null, createProductsDto(), LocalDateTime.now());
        var serializedRequest = objectMapper.writeValueAsString(request);
        var requestFile = new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, serializedRequest.getBytes());

        mockMvc.perform(multipart("/api/v1/shopping-lists")
                                .file(requestFile)
                                .contentType(MediaType.MULTIPART_FORM_DATA))
               .andExpect(status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$.subExceptions[0].field").value("name"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.subExceptions[0].message").value("must not be blank"))
               .andDo(print())
               .andReturn();

    }

    @Test
    @WithUserDetails("test@test.com")
    public void test_CreateShoppingList_ThrowExceptionBadRequest_ProductsAreNotValid() throws Exception {

        var products = List.of(new ProductDto(null, null, -1));
        var request = new CreateShoppingListRequest("shopping_list", products, LocalDateTime.now());
        var serializedRequest = objectMapper.writeValueAsString(request);
        var requestFile = new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, serializedRequest.getBytes());

        mockMvc.perform(multipart("/api/v1/shopping-lists")
                                .file(requestFile)
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
               .andExpect(status().isBadRequest())
               .andDo(print())
               .andReturn();

    }


    @Test
    public void test_Patch_ThrowUnAuthorizedExceptionWhenNoAccessTokenProvided() throws Exception {
        var shoppingList = shoppingLists.stream().findFirst().orElseThrow();


        mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/api/v1/shopping-lists/%s", shoppingList.id()))
                                              .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
               )
               .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("test@test.com")
    public void test_Patch_ShouldThrowExceptionWhenWrongParamTypeIsGiven() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/api/v1/shopping-lists/%s", "abcf"))
                                              .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
               )
               .andExpect(status().isBadRequest())
               .andDo(print());

    }

    @Test
    @WithUserDetails("test@test.com")
    public void test_Patch_UpdateShoppingListAndFile() throws Exception {
        var shoppingList = shoppingLists.stream().findFirst().orElseThrow();
        Files.createDirectories(Paths.get(storageProperties.fullShoppingListLocation()));

        var oldFilename = shoppingList.imageAttachmentFilename();
        File file = new File(storageProperties.fullShoppingListLocation() + '/' + oldFilename);
        file.createNewFile();

        MockMultipartFile attachmentImage = new MockMultipartFile("attachmentImage", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());
        var request = new UpdateShoppingListRequest("shopping_list", createProductsDto(), LocalDateTime.now(), true);
        var serializedRequest = objectMapper.writeValueAsString(request);
        var requestFile = new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE, serializedRequest.getBytes());

        mockMvc.perform(multipart(HttpMethod.PATCH, "/api/v1/shopping-lists/" + shoppingList.id().toString())
                                .file(attachmentImage)
                                .file(requestFile)
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
               .andExpect(status().isOk())
               .andDo(print())
               .andReturn();


        var shoppingListAfterTest = repository.findById(shoppingList.id()).orElseThrow();
        var filePath = storageProperties.fullShoppingListLocation() + "/" + shoppingListAfterTest.imageAttachmentFilename();

        assertNotNull(shoppingListAfterTest);
        assertTrue(shoppingListAfterTest.completed());
        assertEquals("shopping_list", shoppingListAfterTest.name());
        assertNotEquals(oldFilename, shoppingListAfterTest.imageAttachmentFilename());
        assertTrue(Files.exists(Paths.get(filePath)));

        FileUtils.deleteDirectory(Paths.get(storageProperties.getRootLocation()).toFile());
    }
}
